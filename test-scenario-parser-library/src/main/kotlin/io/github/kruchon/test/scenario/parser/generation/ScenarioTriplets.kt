package io.github.kruchon.test.scenario.parser.generation

import io.github.kruchon.test.scenario.parser.syntax.Triplet

internal data class ScenarioTriplets(
    val scenarioName: String,
    val triplets: List<Triplet>
)