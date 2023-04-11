package io.github.kruchon.scenario.configurator

import io.github.kruchon.scenario.configurator.processor.TaskResponse
import io.github.kruchon.scenario.configurator.processor.TestScenarioProcessorClient
import io.github.kruchon.scenario.configurator.project.repository.Project
import io.github.kruchon.scenario.configurator.scenario.repository.Scenario
import java.util.Collections
import java.util.UUID

class TestScenarioProcessorClientStub : TestScenarioProcessorClient {

    private val requestedProjects = Collections.synchronizedList(ArrayList<UUID>())

    fun getRequestedProjects(): List<UUID> {
        return requestedProjects
    }

    override fun processProjectSync(project: Project, scenarios: List<Scenario>): TaskResponse {
        requestedProjects.add(project.id)
        return TaskResponse(scenarios.map {
            TaskResponse.File(it.name, it.content)
        }, checkNotNull(project.id))
    }

    override fun processProjectAsync(project: Project, scenarios: List<Scenario>) {
        TODO("Not yet implemented")
    }
}