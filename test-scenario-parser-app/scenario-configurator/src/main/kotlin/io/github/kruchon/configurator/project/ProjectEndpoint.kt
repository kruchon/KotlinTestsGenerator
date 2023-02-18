package io.github.kruchon.configurator.project

import io.github.kruchon.configurator.project.repository.Project
import io.github.kruchon.configurator.project.view.ProjectView
import java.util.UUID
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/configurator/project")
class ProjectEndpoint(
    private val projectService: ProjectService
) {
    @PostMapping
    fun create(
        @RequestBody createProjectParameters: CreateProjectParameters
    ): ProjectView {
        return projectService.create(createProjectParameters)
    }

    @GetMapping
    fun getAll(): List<ProjectView> {
        return projectService.getAll()
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: UUID): ProjectView {
        return projectService.getById(id)
    }

    @PostMapping("/{id}/process")
    fun process(@PathVariable id: UUID) {
        projectService.process(id)
    }
}