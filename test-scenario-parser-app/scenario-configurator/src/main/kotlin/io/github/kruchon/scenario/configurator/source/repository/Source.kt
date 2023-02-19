package io.github.kruchon.scenario.configurator.source.repository

import io.github.kruchon.scenario.configurator.project.repository.Project
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.ManyToOne

@Entity
class Source(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: UUID? = null,
    var name: String,
    var content: String,
    @ManyToOne
    var project: Project
)