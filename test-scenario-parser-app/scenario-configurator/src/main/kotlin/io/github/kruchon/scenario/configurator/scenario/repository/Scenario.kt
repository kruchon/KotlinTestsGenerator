package io.github.kruchon.scenario.configurator.scenario.repository

import io.github.kruchon.scenario.configurator.project.repository.Project
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import java.util.UUID

@Entity
class Scenario(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: UUID? = null,
    var name: String,
    var content: String,
    @ManyToOne
    var project: Project
)