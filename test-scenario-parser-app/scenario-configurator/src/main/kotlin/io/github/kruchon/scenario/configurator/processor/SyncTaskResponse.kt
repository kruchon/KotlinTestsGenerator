package io.github.kruchon.scenario.configurator.processor

class SyncTaskResponse(
    val files: List<File>
) {
    class File(
        val name: String,
        val content: String
    )
}