package io.github.kruchon.scenario.processor.task.sync

import io.github.kruchon.scenario.configurator.processor.TaskRequest
import io.github.kruchon.scenario.configurator.processor.TaskResponse
import io.github.kruchon.scenario.processor.task.TaskService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/processor/sync/task")
class SyncTaskEndpoint(
    private val taskService: TaskService
) {
    @PostMapping
    fun process(@RequestBody taskRequest: TaskRequest): TaskResponse {
        return taskService.processRequest(taskRequest)
    }
}