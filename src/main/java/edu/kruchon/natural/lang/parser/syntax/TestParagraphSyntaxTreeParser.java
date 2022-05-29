package edu.kruchon.natural.lang.parser.syntax;

import edu.stanford.nlp.ie.util.RelationTriple;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.naturalli.NaturalLogicAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.trees.GrammaticalRelation;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

public enum TestParagraphSyntaxTreeParser {
    INSTANCE;

    private final StanfordCoreNLP pipeline;

    TestParagraphSyntaxTreeParser() {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize,ssplit,pos,lemma,depparse,natlog,openie");
        pipeline = new StanfordCoreNLP(props);
    }

    public Triplet parse(String paragraph) {
        Annotation doc = new Annotation(paragraph);
        pipeline.annotate(doc);
        RelationTriple relationTriple = doc.get(CoreAnnotations.SentencesAnnotation.class).stream()
                .map(
                        coreMap -> coreMap.get(NaturalLogicAnnotations.RelationTriplesAnnotation.class)
                )
                .flatMap(
                        Collection::stream
                )
                .max(
                        Comparator.comparingDouble(f -> f.confidence)
                ).orElseThrow(
                        () -> new RuntimeException("Relation triple was not found")
                );
        SemanticGraph dependencyTree = relationTriple.asDependencyTree().orElseThrow(() -> new RuntimeException("Error building dependency tree"));
        IndexedWord relationshipNode = dependencyTree.getFirstRoot();
        IndexedWord object = dependencyTree.getChildrenWithReln(relationshipNode, GrammaticalRelation.valueOf("obj")).iterator().next();
        return new Triplet(relationTriple.subjectLemmaGloss(), relationTriple.relationLemmaGloss(), processNode(object, dependencyTree));
    }

    private Parameter processNode(IndexedWord node, SemanticGraph dependencyTree) {
        // todo fix the case with needless nmods
        Set<IndexedWord> childrenNodes = dependencyTree.getChildren(node);
        List<Parameter> childrenParameterNodes = childrenNodes.stream()
                .filter(childrenNode -> !dependencyTree.getChildren(childrenNode).isEmpty())
                .sorted(Comparator.comparingInt(IndexedWord::beginPosition))
                .map(childrenNode -> processNode(childrenNode, dependencyTree))
                .collect(Collectors.toList());
        List<String> childrenValueNodes = childrenNodes.stream()
                .filter(childrenNode -> dependencyTree.getChildren(childrenNode).isEmpty())
                .sorted(Comparator.comparingInt(IndexedWord::beginPosition))
                .map(IndexedWord::originalText)
                .collect(Collectors.toList());
        return new Parameter(node.originalText(), childrenValueNodes, childrenParameterNodes);
    }
}
