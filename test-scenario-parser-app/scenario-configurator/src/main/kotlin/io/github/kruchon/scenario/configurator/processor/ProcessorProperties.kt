package io.github.kruchon.scenario.configurator.processor

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties(prefix = "processor")
@ConstructorBinding
class ProcessorProperties(
    val url: String
)