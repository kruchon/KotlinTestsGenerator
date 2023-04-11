package io.github.kruchon.scenario.configurator.processor

import io.github.kruchon.scenario.configurator.project.repository.Project
import io.github.kruchon.scenario.configurator.scenario.repository.Scenario
import java.util.concurrent.Future
import kotlinx.coroutines.Deferred
import org.springframework.util.concurrent.ListenableFuture

interface TestScenarioProcessorClient {
    fun processProjectSync(project: Project, scenarios: List<Scenario>): TaskResponse
    fun processProjectAsync(project: Project, scenarios: List<Scenario>)
}