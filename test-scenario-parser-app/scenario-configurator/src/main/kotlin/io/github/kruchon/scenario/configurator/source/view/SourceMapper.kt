package io.github.kruchon.scenario.configurator.source.view

import io.github.kruchon.scenario.configurator.source.repository.SourceRepository
import java.util.UUID
import org.springframework.stereotype.Component

@Component
class SourceMapper(
    private val sourceRepository: SourceRepository
) {
    fun toView(sourceId: UUID): SourceView {
        val source = sourceRepository.getReferenceById(sourceId)
        return SourceView(checkNotNull(source.id), source.name, source.content)
    }
}