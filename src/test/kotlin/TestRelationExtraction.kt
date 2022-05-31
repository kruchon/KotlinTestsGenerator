import io.github.test.scenario.parser.generation.KotlinGenerationProperties
import org.junit.Test

class TestRelationExtraction {
    @Test
    fun test() {
        KotlinGenerationProperties.implementationPackage = "test.package"
        print(TestGenerator.generate("User paid enterprice tariff of price 500. User got bill 500"))
    }
}