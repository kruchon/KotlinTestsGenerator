package io.github.kruchon.scenario.configurator.project

import io.github.kruchon.scenario.configurator.processor.TaskResponse
import io.github.kruchon.scenario.configurator.processor.TestScenarioProcessorClient
import io.github.kruchon.scenario.configurator.project.repository.Project
import io.github.kruchon.scenario.configurator.project.repository.ProjectRepository
import io.github.kruchon.scenario.configurator.project.view.ProjectMapper
import io.github.kruchon.scenario.configurator.project.view.ProjectView
import io.github.kruchon.scenario.configurator.scenario.repository.ScenarioRepository
import io.github.kruchon.scenario.configurator.source.repository.Source
import io.github.kruchon.scenario.configurator.source.repository.SourceRepository
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

    fun processSync(projectId: UUID) {
        val scenarios = scenarioRepository.findByProjectId(projectId)
        val syncTaskResponse = testScenarioProcessorClient.processProjectSync(projectRepository.getReferenceById(projectId), scenarios)
        saveTaskResponse(syncTaskResponse)
    }

    fun saveTaskResponse(taskResponse: TaskResponse) {
        transactionTemplate.execute {
            val projectId = taskResponse.projectId
            val project = projectRepository.getReferenceById(projectId)
            val sources = taskResponse.files.map { Source(null, it.name, it.content, project) }
            sourceRepository.deleteAllByProjectId(projectId)
            sourceRepository.saveAll(sources)
        }
    }

    fun processAsync(id: UUID) {
        val scenarios = scenarioRepository.findByProjectId(id)
        testScenarioProcessorClient.processProjectAsync(projectRepository.getReferenceById(id), scenarios)
    }
}