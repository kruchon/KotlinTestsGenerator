package io.github.kruchon.configurator

import io.github.kruchon.configurator.processor.SyncTaskResponse
import io.github.kruchon.configurator.processor.TestScenarioProcessorClient
import io.github.kruchon.configurator.project.repository.Project
import io.github.kruchon.configurator.scenario.repository.Scenario
import java.util.Collections
import java.util.UUID

class TestScenarioProcessorClientStub: TestScenarioProcessorClient {

    private val requestedProjects = Collections.synchronizedList(ArrayList<UUID>())

    fun getRequestedProjects(): List<UUID> {
        return requestedProjects
    }

    override fun processProjectSync(project: Project, scenarios: List<Scenario>): SyncTaskResponse {
        requestedProjects.add(project.id)
        return SyncTaskResponse(scenarios.map {
            SyncTaskResponse.File(it.name, it.content)
        })
    }
}