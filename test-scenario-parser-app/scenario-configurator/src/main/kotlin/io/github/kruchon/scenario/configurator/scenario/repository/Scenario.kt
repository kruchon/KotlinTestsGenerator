package io.github.kruchon.scenario.configurator.scenario.repository

import io.github.kruchon.scenario.configurator.project.repository.Project
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.ManyToOne

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