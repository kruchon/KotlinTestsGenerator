package io.github.kruchon.scenario.processor.task.sync

class SyncTaskRequest(
    val scenarios: List<Scenario>,
    val generationPackage: String,
    val implementationPackage: String
) {
    class Scenario(
        val name: String,
        val content: String
    )
}