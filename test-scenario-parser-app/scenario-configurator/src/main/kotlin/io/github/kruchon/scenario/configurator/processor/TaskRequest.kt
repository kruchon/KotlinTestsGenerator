package io.github.kruchon.scenario.configurator.processor

import java.util.UUID

class TaskRequest(
    val scenarios: List<Scenario>,
    val generationPackage: String,
    val implementationPackage: String,
    val projectId: UUID
) {
    class Scenario(
        val name: String,
        val content: String
    )
}