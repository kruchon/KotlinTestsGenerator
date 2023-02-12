package io.github.kruchon.test.scenario.parser

import io.github.kruchon.test.scenario.parser.api.TestScenarioParser
import io.github.kruchon.test.scenario.parser.generation.KotlinGenerationDefaultProperties
import io.github.kruchon.test.scenario.parser.generation.KotlinSource
import org.junit.Assert.assertEquals
import org.junit.Before
import java.io.File
import org.junit.Test

class KotlinSourcesGenerationTest {
    @Before
    fun setUp() {
        KotlinGenerationDefaultProperties.generationPackage = "io.github.kruchon"
        KotlinGenerationDefaultProperties.implementationPackage = "test.package"
    }

    @Test
    fun `object without params`() {
        generateKotlinSourcesAndCompareWithExpected("object_without_params")
    }

    @Test
    fun `object with params`() {
        generateKotlinSourcesAndCompareWithExpected("object_with_params")
    }

    @Test
    fun `mixed object with value and param`() {
        generateKotlinSourcesAndCompareWithExpected("mixed_object_with_value_and_param")
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