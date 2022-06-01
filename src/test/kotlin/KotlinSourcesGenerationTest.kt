import io.github.kruchon.test.scenario.parser.generation.KotlinGenerationProperties
import io.github.kruchon.test.scenario.parser.generation.KotlinSource
import java.io.File
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class KotlinSourcesGenerationTest {

    @Before
    fun setUp() {
        KotlinGenerationProperties.implementationPackage = "test.package"
    }

    @Test
    fun `object without params`() {
        generateKotlinSourcesAndCompareWithExpected("object_without_params")
    }

    @Test
    fun `object with params`() {
        generateKotlinSourcesAndCompareWithExpected("object_with_params")
    }

    private fun generateKotlinSourcesAndCompareWithExpected(testResourceDirectory: String) {
        val inputDirectory = "src/test/resources/scenarios/${testResourceDirectory}"
        val kotlinSourcesDirectory = "$inputDirectory/sources"
        val input = File("$inputDirectory/input.txt").readText()
        val expectedSources = checkNotNull(File(kotlinSourcesDirectory).listFiles()).map {
            KotlinSource(
                it.name,
                it.readText(Charsets.UTF_8)
            )
        }.toList().sortedBy { it.name }
        val actualSources = TestScenarioParser.parse(input).sources.sortedBy { it.name }
        assertEquals(expectedSources, actualSources)
    }
}