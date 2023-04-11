package io.github.kruchon.scenario.configurator.processor

import io.github.kruchon.scenario.configurator.project.ProjectService
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class TaskResponseListener(
    private val projectService: ProjectService
) {
    @KafkaListener(id = "task_response_receiver", topics = ["task_responses"], groupId = "task_responses_saving", containerFactory = "kafkaTaskResponseListenerFactory")
    fun listen(taskResponse: TaskResponse) {
        projectService.saveTaskResponse(taskResponse)
    }
}