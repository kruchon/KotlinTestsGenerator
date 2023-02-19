package io.github.kruchon.scenario.configurator.project.view

import io.github.kruchon.scenario.configurator.project.repository.ProjectRepository
import io.github.kruchon.scenario.configurator.scenario.repository.ScenarioRepository
import io.github.kruchon.scenario.configurator.scenario.view.ScenarioMapper
import io.github.kruchon.scenario.configurator.source.repository.SourceRepository
import io.github.kruchon.scenario.configurator.source.view.SourceMapper
import java.util.UUID
import org.springframework.stereotype.Component

@Component
class ProjectMapper(
    private val sourceMapper: SourceMapper,
    private val scenarioMapper: ScenarioMapper,
    private val scenarioRepository: ScenarioRepository,
    private val projectRepository: ProjectRepository,
    private val sourceRepository: SourceRepository
) {
    fun toView(projectId: UUID): ProjectView {
        val project = projectRepository.getReferenceById(projectId)
        val scenarios = scenarioRepository.findByProjectId(projectId)
        val sources = sourceRepository.findByProjectId(projectId)
        return ProjectView(
            checkNotNull(project.id),
            project.name,
            project.generationPackage,
            project.implementationPackage,
            scenarios.map {
                scenarioMapper.toView(checkNotNull(it.id))
            },
            sources.map {
                sourceMapper.toView(checkNotNull(it.id))
            }
        )
    }
}