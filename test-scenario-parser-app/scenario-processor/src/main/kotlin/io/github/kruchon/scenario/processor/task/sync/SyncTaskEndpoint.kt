package io.github.kruchon.scenario.processor.task.sync

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/processor/sync/task")
class SyncTaskEndpoint(
    private val syncTaskService: SyncTaskService
) {
    @PostMapping
    fun process(@RequestBody syncTaskRequest: SyncTaskRequest): SyncTaskResponse {
        return syncTaskService.processRequests(syncTaskRequest)
    }
}