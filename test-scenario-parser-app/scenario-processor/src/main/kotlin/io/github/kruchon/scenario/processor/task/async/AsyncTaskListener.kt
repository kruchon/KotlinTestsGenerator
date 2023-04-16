package io.github.kruchon.scenario.processor.task.async

import io.github.kruchon.scenario.configurator.processor.TaskRequest
import io.github.kruchon.scenario.processor.task.TaskService
import io.github.kruchon.scenario.configurator.processor.TaskResponse
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component


@Component
class AsyncTaskListener(
    private val taskService: TaskService,
    private val taskResponseKafkaTemplate: KafkaTemplate<String, TaskResponse>
) {
    @KafkaListener(concurrency = "2", idIsGroup = false, topics = ["task_requests"], groupId = "task_requests_processing", containerFactory = "kafkaTaskRequestListenerFactory")
    fun listenFirst(taskRequest: TaskRequest) {
        val taskResponse = taskService.processRequest(taskRequest)
        taskResponseKafkaTemplate.send("task_responses", taskResponse)
    }
}