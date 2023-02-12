package io.github.kruchon.scenario.processor.task.sync

class SyncTaskResponse(
    private val files: List<File>
) {
    class File(
        val name: String,
        val content: String
    )
}