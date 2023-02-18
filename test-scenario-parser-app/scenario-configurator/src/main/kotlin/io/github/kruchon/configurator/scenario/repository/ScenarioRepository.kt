package io.github.kruchon.configurator.scenario.repository

import java.util.UUID
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ScenarioRepository : JpaRepository<Scenario, UUID> {
    fun findByProjectId(projectId: UUID): List<Scenario>
}