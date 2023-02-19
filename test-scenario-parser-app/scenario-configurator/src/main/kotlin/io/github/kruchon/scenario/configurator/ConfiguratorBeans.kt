package io.github.kruchon.scenario.configurator

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.github.kruchon.scenario.configurator.processor.ProcessorProperties
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement
import org.springframework.transaction.support.TransactionTemplate
import org.springframework.web.client.RestTemplate


@Configuration
@EnableTransactionManagement
class ConfiguratorBeans {
    @Bean
    fun objectMapper(): ObjectMapper {
        val objectMapper = ObjectMapper()
        return objectMapper.registerModule(JavaTimeModule())
            .registerModule(KotlinModule.Builder().build())
    }

    @Bean
    fun restTemplate(processorProperties: ProcessorProperties): RestTemplate {
        return RestTemplateBuilder().rootUri(processorProperties.url).build()
    }

    @Bean
    fun transactionTemplate(transactionManager: PlatformTransactionManager): TransactionTemplate {
        return TransactionTemplate(transactionManager)
    }
}