package io.github.kruchon.scenario.processor.task.sync

class SyncTaskRequest(
    val scenarios: List<Scenario>
) {
    class Scenario(
        val name: String,
        val content: String
    )
}