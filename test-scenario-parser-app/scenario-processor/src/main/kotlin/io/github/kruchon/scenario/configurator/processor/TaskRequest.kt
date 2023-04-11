package io.github.kruchon.scenario.configurator.processor

import java.util.UUID

data class TaskRequest(
    val scenarios: List<Scenario>,
    val generationPackage: String,
    val implementationPackage: String,
    val projectId: UUID
) {
    data class Scenario(
        val name: String,
        val content: String
    )
}