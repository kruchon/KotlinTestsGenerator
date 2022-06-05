package io.github.kruchon.test.scenario.parser.syntax

import edu.stanford.nlp.ling.CoreAnnotations
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation
import edu.stanford.nlp.ling.IndexedWord
import edu.stanford.nlp.naturalli.NaturalLogicAnnotations.RelationTriplesAnnotation
import edu.stanford.nlp.pipeline.Annotation
import edu.stanford.nlp.pipeline.StanfordCoreNLP
import edu.stanford.nlp.semgraph.SemanticGraph
import java.util.Properties

internal object TestParagraphSyntaxTreeParser {

    private val pipeline: StanfordCoreNLP

    init {
        val props = Properties()
        props.setProperty("annotators", "tokenize,ssplit,pos,lemma,depparse,natlog,openie")
        pipeline = StanfordCoreNLP(props)
    }

    private val needlessDependencies = setOf(
        "case"
    )

    fun parse(paragraph: String): Triplet {
        val doc = Annotation(paragraph)
        pipeline.annotate(doc)
        val relationTriple = checkNotNull(doc.get(SentencesAnnotation::class.java)
            .map { it.get(RelationTriplesAnnotation::class.java) }
            .flatten()
            .filter { it.confidence > 0.7 }
            .maxByOrNull { (it.objectGloss() + it.relationGloss() + it.subjectGloss()).length }
        )
        val dependencyTree =
            relationTriple.asDependencyTree().orElseThrow { RuntimeException("Error building dependency tree") }
        val relationshipNode = dependencyTree.firstRoot
        val objectNodes = relationTriple.`object`.map { it.originalText() }
        val `object`: IndexedWord = dependencyTree.getChildren(relationshipNode)
            .first { objectNodes.contains(it.originalText()) }

        return Triplet(
            relationTriple.subjectLemmaGloss(),
            relationTriple.relationLemmaGloss(),
            processNode(`object`, dependencyTree)
        )
    }

    private fun processNode(node: IndexedWord, dependencyTree: SemanticGraph): Parameter {
        // todo fix the case with needless nmods
        val childrenNodes = getSatisfyingChildrenNodes(node, dependencyTree)
        val childrenParameterNodes = childrenNodes
            .filter { childrenNode -> getSatisfyingChildrenNodes(childrenNode, dependencyTree).isNotEmpty() }
            .sortedBy {
                it.beginPosition()
            }
            .map { childrenNode -> processNode(childrenNode, dependencyTree) }
        val childrenValueNodes = childrenNodes
            .filter { childrenNode -> getSatisfyingChildrenNodes(childrenNode, dependencyTree).isEmpty() }
            .sortedBy {
                it.beginPosition()
            }
            .map(IndexedWord::originalText)
        return Parameter(node.originalText(), childrenValueNodes, childrenParameterNodes)
    }

    private fun getSatisfyingChildrenNodes(node: IndexedWord, dependencyTree: SemanticGraph): List<IndexedWord> {
        return dependencyTree.getChildren(node).filter {
            !needlessDependencies.contains(dependencyTree.getEdge(node, it).relation.shortName)
        }
    }
}