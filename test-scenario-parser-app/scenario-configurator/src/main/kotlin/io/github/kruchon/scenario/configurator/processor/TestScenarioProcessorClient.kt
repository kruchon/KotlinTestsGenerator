package io.github.kruchon.scenario.configurator.processor

import io.github.kruchon.scenario.configurator.project.repository.Project
import io.github.kruchon.scenario.configurator.scenario.repository.Scenario

interface TestScenarioProcessorClient {
    fun processProjectSync(project: Project, scenarios: List<Scenario>): SyncTaskResponse
}