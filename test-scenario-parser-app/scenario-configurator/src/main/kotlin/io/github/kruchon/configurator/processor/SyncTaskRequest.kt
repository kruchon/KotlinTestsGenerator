package io.github.kruchon.configurator.processor

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