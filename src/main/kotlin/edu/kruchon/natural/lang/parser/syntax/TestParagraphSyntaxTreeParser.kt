package edu.kruchon.natural.lang.parser.syntax

import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation
import edu.stanford.nlp.ling.IndexedWord
import edu.stanford.nlp.naturalli.NaturalLogicAnnotations.RelationTriplesAnnotation
import edu.stanford.nlp.pipeline.Annotation
import edu.stanford.nlp.pipeline.StanfordCoreNLP
import edu.stanford.nlp.semgraph.SemanticGraph
import edu.stanford.nlp.trees.GrammaticalRelation
import java.util.*

object TestParagraphSyntaxTreeParser {

    private val pipeline: StanfordCoreNLP

    init {
        val props = Properties()
        props.setProperty("annotators", "tokenize,ssplit,pos,lemma,depparse,natlog,openie")
        pipeline = StanfordCoreNLP(props)
    }

    fun parse(paragraph: String): Triplet {
        val doc = Annotation(paragraph)
        pipeline.annotate(doc)
        val relationTriple = checkNotNull(doc.get(SentencesAnnotation::class.java)
                .map { it.get(RelationTriplesAnnotation::class.java) }
                .flatten()
                .maxByOrNull { it.confidence })
        val dependencyTree: SemanticGraph = relationTriple.asDependencyTree().orElseThrow { RuntimeException("Error building dependency tree") }
        val relationshipNode = dependencyTree.firstRoot
        val `object`: IndexedWord = dependencyTree.getChildrenWithReln(relationshipNode, GrammaticalRelation.valueOf("obj")).iterator().next()
        return Triplet(relationTriple.subjectLemmaGloss(), relationTriple.relationLemmaGloss(), processNode(`object`, dependencyTree))
    }

    private fun processNode(node: IndexedWord, dependencyTree: SemanticGraph): Parameter {
        // todo fix the case with needless nmods
        val childrenNodes = dependencyTree.getChildren(node)
        val childrenParameterNodes = childrenNodes
                .filter { childrenNode -> dependencyTree.getChildren(childrenNode).isNotEmpty() }
                .sortedBy {
                    it.beginPosition()
                }
                .map { childrenNode -> processNode(childrenNode, dependencyTree) }
        val childrenValueNodes = childrenNodes
                .filter { childrenNode -> dependencyTree.getChildren(childrenNode).isEmpty() }
                .sortedBy {
                    it.beginPosition()
                }
                .map(IndexedWord::originalText)
        return Parameter(node.originalText(), childrenValueNodes, childrenParameterNodes)
    }
}