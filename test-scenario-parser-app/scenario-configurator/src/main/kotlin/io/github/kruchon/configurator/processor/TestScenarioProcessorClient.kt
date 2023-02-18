package io.github.kruchon.configurator.processor

import io.github.kruchon.configurator.project.repository.Project
import io.github.kruchon.configurator.scenario.repository.Scenario

interface TestScenarioProcessorClient {
    fun processProjectSync(project: Project, scenarios: List<Scenario>): SyncTaskResponse
}