package io.github.kruchon.scenario.processor

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.github.kruchon.scenario.configurator.processor.TaskRequest
import io.github.kruchon.scenario.configurator.processor.TaskResponse
import org.apache.kafka.clients.admin.NewTopic
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
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


@Configuration
class ProcessorBeans {
    @Bean
    fun objectMapper(): ObjectMapper {
        val objectMapper = ObjectMapper()
        return objectMapper.registerModule(JavaTimeModule())
            .registerModule(KotlinModule.Builder().build())
    }

    @Bean
    fun topic(): NewTopic {
        return TopicBuilder.name("task_requests")
            .partitions(1)
            .replicas(1)
            .build()
    }

    @Bean
    fun taskRequestConsumerFactory(): ConsumerFactory<String, TaskRequest> {
        val props: MutableMap<String, Any> = HashMap()
        props[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = "localhost:9092"
        props[ConsumerConfig.GROUP_ID_CONFIG] = "task_requests_processing"
        props[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        props[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = org.springframework.kafka.support.serializer.JsonDeserializer::class.java
        props[JsonDeserializer.TRUSTED_PACKAGES] = "*"
        return DefaultKafkaConsumerFactory(props)
    }

    @Bean
    fun kafkaTaskRequestListenerFactory(taskRequestConsumerFactory: ConsumerFactory<String, TaskRequest>): ConcurrentKafkaListenerContainerFactory<String, TaskRequest> {
        val factory = ConcurrentKafkaListenerContainerFactory<String, TaskRequest>()
        factory.consumerFactory = taskRequestConsumerFactory
        return factory
    }

    @Bean
    fun taskResponseProducerFactory(): ProducerFactory<String, TaskResponse> {
        val configProps: MutableMap<String, Any> = HashMap()
        configProps[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = "localhost:9092"
        configProps[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
        configProps[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = org.springframework.kafka.support.serializer.JsonSerializer::class.java
        return DefaultKafkaProducerFactory(configProps)
    }

    @Bean
    fun taskResponseKafkaTemplate(taskResponseProducerFactory: ProducerFactory<String, TaskResponse>): KafkaTemplate<String, TaskResponse> {
        return KafkaTemplate(taskResponseProducerFactory)
    }
}