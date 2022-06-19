package io.github.kruchon.test.scenario.parser.api

import io.github.kruchon.test.scenario.parser.generation.KotlinSourceGenerator
import io.github.kruchon.test.scenario.parser.generation.ScenarioTriplets
import io.github.kruchon.test.scenario.parser.generation.TestScenarioParsingResult
import io.github.kruchon.test.scenario.parser.syntax.TestParagraphSyntaxTreeParser
import io.github.kruchon.test.scenario.parser.syntax.Triplet

object TestScenarioParser {
    fun parse(testScenarioContent: String, testScenarioName: String = "Test"): TestScenarioParsingResult {
        val triplets = TestParagraphSyntaxTreeParser.parse(testScenarioContent)
        val scenarioTriplets = ScenarioTriplets(testScenarioName, triplets)
        return KotlinSourceGenerator.generateSingleScenario(scenarioTriplets)
    }

    fun parse(scenarios: List<TestScenario>) {

    }
}