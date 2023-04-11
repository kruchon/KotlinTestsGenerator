package io.github.kruchon.scenario.configurator

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.github.kruchon.scenario.configurator.processor.ProcessorProperties
import io.github.kruchon.scenario.configurator.processor.TaskRequest
import io.github.kruchon.scenario.configurator.processor.TaskResponse
import org.apache.kafka.clients.admin.NewTopic
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.config.TopicBuilder
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory
import org.springframework.kafka.support.serializer.JsonDeserializer
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
    fun topic(): NewTopic {
        return TopicBuilder.name("task_responses")
            .partitions(1)
            .replicas(1)
            .build()
    }

    @Bean
    fun transactionTemplate(transactionManager: PlatformTransactionManager): TransactionTemplate {
        return TransactionTemplate(transactionManager)
    }

    @Bean
    fun taskRequestProducerFactory(): ProducerFactory<String, TaskRequest> {
        val configProps: MutableMap<String, Any> = HashMap()
        configProps[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = "localhost:9092"
        configProps[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
        configProps[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = org.springframework.kafka.support.serializer.JsonSerializer::class.java
        return DefaultKafkaProducerFactory(configProps)
    }

    @Bean
    fun taskRequestKafkaTemplate(taskRequestProducerFactory: ProducerFactory<String, TaskRequest>): KafkaTemplate<String, TaskRequest> {
        return KafkaTemplate(taskRequestProducerFactory)
    }

    @Bean
    fun taskResponseConsumerFactory(): ConsumerFactory<String, TaskResponse> {
        val props: MutableMap<String, Any> = HashMap()
        props[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = "localhost:9092"
        props[ConsumerConfig.GROUP_ID_CONFIG] = "task_responses_saving"
        props[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        props[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = org.springframework.kafka.support.serializer.JsonDeserializer::class.java
        props[JsonDeserializer.TRUSTED_PACKAGES] = "*"
        return DefaultKafkaConsumerFactory(props)
    }

    @Bean
    fun kafkaTaskResponseListenerFactory(taskResponseConsumerFactory: ConsumerFactory<String, TaskResponse>): ConcurrentKafkaListenerContainerFactory<String, TaskResponse> {
        val factory = ConcurrentKafkaListenerContainerFactory<String, TaskResponse>()
        factory.consumerFactory = taskResponseConsumerFactory
        return factory
    }
}