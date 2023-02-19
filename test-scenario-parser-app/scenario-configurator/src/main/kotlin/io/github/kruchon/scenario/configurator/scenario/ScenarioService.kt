package io.github.kruchon.scenario.configurator.scenario

import io.github.kruchon.scenario.configurator.project.repository.ProjectRepository
import io.github.kruchon.scenario.configurator.scenario.repository.Scenario
import io.github.kruchon.scenario.configurator.scenario.repository.ScenarioRepository
import io.github.kruchon.scenario.configurator.scenario.view.ScenarioMapper
import io.github.kruchon.scenario.configurator.scenario.view.ScenarioView
import java.util.UUID
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ScenarioService(
    private val scenarioRepository: ScenarioRepository,
    private val projectRepository: ProjectRepository,
    private val scenarioMapper: ScenarioMapper
) {
    @Transactional
    fun create(projectId: UUID, createScenarioParameters: CreateScenarioParameters): ScenarioView {
        val project = projectRepository.getReferenceById(projectId)
        val scenario = Scenario(null, createScenarioParameters.name, createScenarioParameters.content, project)
        scenarioRepository.save(scenario)
        return scenarioMapper.toView(checkNotNull(scenario.id))
    }
}