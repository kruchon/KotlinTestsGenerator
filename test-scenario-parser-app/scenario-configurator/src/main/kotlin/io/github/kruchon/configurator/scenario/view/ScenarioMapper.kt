package io.github.kruchon.configurator.scenario.view

import io.github.kruchon.configurator.scenario.repository.ScenarioRepository
import java.util.UUID
import org.springframework.stereotype.Component

@Component
class ScenarioMapper(
    private val scenarioRepository: ScenarioRepository
) {
    fun toView(scenarioId: UUID): ScenarioView {
        val scenario = scenarioRepository.getReferenceById(scenarioId)
        return ScenarioView(checkNotNull(scenario.id), scenario.name, scenario.content)
    }
}