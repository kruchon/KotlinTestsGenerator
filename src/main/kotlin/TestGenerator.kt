import io.github.test.scenario.parser.generation.KotlinSource
import io.github.test.scenario.parser.generation.KotlinSourceGenerator
import io.github.test.scenario.parser.syntax.TestParagraphSyntaxTreeParser
import io.github.test.scenario.parser.syntax.Triplet

object TestGenerator {
    fun generate(testScenario: String): List<KotlinSource> {
        val paragraphs = testScenario.split(".")
        val triplets = mutableListOf<Triplet>()
        for (paragraph in paragraphs) {
            triplets.add(TestParagraphSyntaxTreeParser.parse(paragraph))
        }
        return KotlinSourceGenerator.generate(triplets)
    }
}