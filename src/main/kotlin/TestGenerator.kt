import edu.kruchon.natural.lang.parser.codegeneration.KotlinSource
import edu.kruchon.natural.lang.parser.codegeneration.KotlinSourceGenerator
import edu.kruchon.natural.lang.parser.syntax.TestParagraphSyntaxTreeParser
import edu.kruchon.natural.lang.parser.syntax.Triplet

object TestGenerator {
    fun generate(testScenario: String): List<KotlinSource> {
        val paragraphs = testScenario.split("\\.")
        val triplets = mutableListOf<Triplet>()
        for (paragraph in paragraphs) {
            triplets.add(TestParagraphSyntaxTreeParser.parse(paragraph))
        }
        return KotlinSourceGenerator.generate(triplets)
    }
}