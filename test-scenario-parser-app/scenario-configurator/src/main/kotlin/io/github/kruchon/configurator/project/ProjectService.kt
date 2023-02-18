package io.github.kruchon.configurator.project

import io.github.kruchon.configurator.processor.TestScenarioProcessorClient
import io.github.kruchon.configurator.project.repository.Project
import io.github.kruchon.configurator.project.repository.ProjectRepository
import io.github.kruchon.configurator.project.view.ProjectMapper
import io.github.kruchon.configurator.project.view.ProjectView
import io.github.kruchon.configurator.scenario.repository.ScenarioRepository
import io.github.kruchon.configurator.source.repository.Source
import io.github.kruchon.configurator.source.repository.SourceRepository
import java.util.UUID
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.support.TransactionTemplate

@Service
class ProjectService(
    private val projectRepository: ProjectRepository,
    private val testScenarioProcessorClient: TestScenarioProcessorClient,
    private val transactionTemplate: TransactionTemplate,
    private val sourceRepository: SourceRepository,
    private val projectMapper: ProjectMapper,
    private val scenarioRepository: ScenarioRepository
) {
    @Transactional
    fun create(createProjectParameters: CreateProjectParameters): ProjectView {
        val project = Project(null, createProjectParameters.name, createProjectParameters.generationPackage, createProjectParameters.implementationPackage)
        projectRepository.save(project)
        return projectMapper.toView(checkNotNull(project.id))
    }

    fun getAll(): List<ProjectView> {
        return projectRepository.findAll().map { projectMapper.toView(checkNotNull(it.id)) }
    }

    fun getById(id: UUID): ProjectView {
        return projectRepository.getReferenceById(id).let { projectMapper.toView(checkNotNull(it.id)) }
    }

    fun process(id: UUID) {
        val scenarios = scenarioRepository.findByProjectId(id)
        val syncTaskResponse = testScenarioProcessorClient.processProjectSync(projectRepository.getReferenceById(id), scenarios)
        transactionTemplate.execute {
            val project = projectRepository.getReferenceById(id)
            val sources = syncTaskResponse.files.map { Source(null, it.name, it.content, project) }
            sourceRepository.saveAll(sources)
        }
    }
}