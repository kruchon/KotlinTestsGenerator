package io.github.kruchon.scenario.configurator.processor

import java.util.UUID

class TaskResponse(
    val files: List<File>,
    val projectId: UUID
) {
    class File(
        val name: String,
        val content: String
    )
}