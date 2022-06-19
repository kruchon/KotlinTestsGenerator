package io.github.kruchon.test.scenario.parser.api

import io.github.kruchon.test.scenario.parser.generation.KotlinSourceGenerator
import io.github.kruchon.test.scenario.parser.generation.ScenarioTriplets
import io.github.kruchon.test.scenario.parser.generation.TestScenarioParsingResult
import io.github.kruchon.test.scenario.parser.syntax.TestParagraphSyntaxTreeParser

object TestScenarioParser {
    fun parse(testScenarioContent: String, testScenarioName: String = "Test"): TestScenarioParsingResult {
        val triplets = TestParagraphSyntaxTreeParser.parse(testScenarioContent)
        val scenarioTriplets = ScenarioTriplets(testScenarioName, triplets)
        return KotlinSourceGenerator.generateSingleScenario(scenarioTriplets)
    }

    fun parse(scenarios: List<TestScenario>): TestScenarioParsingResult {
        val scenarioTripletsList = scenarios.map {
            val triplets = TestParagraphSyntaxTreeParser.parse(it.content)
            ScenarioTriplets(it.name, triplets)
        }
        return KotlinSourceGenerator.generateScenarios(scenarioTripletsList)
    }
}