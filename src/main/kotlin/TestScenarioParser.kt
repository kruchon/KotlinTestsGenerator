import io.github.kruchon.test.scenario.parser.generation.KotlinSourceGenerator
import io.github.kruchon.test.scenario.parser.generation.TestScenarioParsingResult
import io.github.kruchon.test.scenario.parser.syntax.TestParagraphSyntaxTreeParser
import io.github.kruchon.test.scenario.parser.syntax.Triplet

object TestScenarioParser {
    fun parse(testScenarioName: String = "Test", testScenarioContent: String): TestScenarioParsingResult {
        val paragraphs = testScenarioContent.removeSuffix(".").split(".")
        val triplets = mutableListOf<Triplet>()
        for (paragraph in paragraphs) {
            triplets.add(TestParagraphSyntaxTreeParser.parse(paragraph))
        }
        return KotlinSourceGenerator.generateSingleScenario(triplets)
    }
}