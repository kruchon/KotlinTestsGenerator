package io.github.kruchon.configurator.source.repository

import io.github.kruchon.configurator.project.repository.Project
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import java.util.UUID

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