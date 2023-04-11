package io.github.kruchon.scenario.configurator.processor

import io.github.kruchon.scenario.configurator.project.repository.Project
import io.github.kruchon.scenario.configurator.scenario.repository.Scenario
import java.util.UUID
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class TestScenarioProcessorClientImpl(
    private val restTemplate: RestTemplate,
    private val taskRequestKafkaTemplate: KafkaTemplate<String, TaskRequest>
) : TestScenarioProcessorClient {

    override fun processProjectSync(project: Project, scenarios: List<Scenario>): TaskResponse {
        val syncTaskRequest = TaskRequest(
            scenarios.map { TaskRequest.Scenario(it.name, it.content) },
            project.generationPackage,
            project.implementationPackage,
            checkNotNull(project.id)
        )
        return checkNotNull(
            restTemplate.postForObject(
                "/api/processor/sync/task",
                syncTaskRequest,
                TaskResponse::class.java
            )
        )
    }

    override fun processProjectAsync(project: Project, scenarios: List<Scenario>) {
        val taskRequest = TaskRequest(
            scenarios.map { TaskRequest.Scenario(it.name, it.content) },
            project.generationPackage,
            project.implementationPackage,
            checkNotNull(project.id)
        )
        taskRequestKafkaTemplate.send("task_requests", taskRequest)
    }
}