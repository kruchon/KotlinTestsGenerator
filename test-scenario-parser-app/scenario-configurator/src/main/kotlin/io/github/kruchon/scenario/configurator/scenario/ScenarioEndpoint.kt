package io.github.kruchon.scenario.configurator.scenario

import io.github.kruchon.scenario.configurator.scenario.view.ScenarioView
import java.util.UUID
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/configurator/project/{projectId}/scenario")
class ScenarioEndpoint(
    private val scenarioService: ScenarioService
) {
    @PostMapping
    fun create(@PathVariable projectId: UUID, @RequestBody createScenarioParameters: CreateScenarioParameters): ScenarioView {
        return scenarioService.create(projectId, createScenarioParameters)
    }
}