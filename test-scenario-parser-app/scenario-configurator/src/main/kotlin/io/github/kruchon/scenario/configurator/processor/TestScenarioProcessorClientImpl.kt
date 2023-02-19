package io.github.kruchon.scenario.configurator.processor

import io.github.kruchon.scenario.configurator.project.repository.Project
import io.github.kruchon.scenario.configurator.scenario.repository.Scenario
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class TestScenarioProcessorClientImpl(
    private val restTemplate: RestTemplate
) : TestScenarioProcessorClient {
    override fun processProjectSync(project: Project, scenarios: List<Scenario>): SyncTaskResponse {
        val syncTaskRequest = SyncTaskRequest(
            scenarios.map { SyncTaskRequest.Scenario(it.name, it.content) },
            project.generationPackage,
            project.implementationPackage
        )
        return checkNotNull(
            restTemplate.postForObject(
                "/api/processor/sync/task",
                syncTaskRequest,
                SyncTaskResponse::class.java
            )
        )
    }
}