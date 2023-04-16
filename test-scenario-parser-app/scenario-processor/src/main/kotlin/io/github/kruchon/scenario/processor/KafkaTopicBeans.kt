package io.github.kruchon.scenario.processor

import org.apache.kafka.clients.admin.NewTopic
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.TopicBuilder

@Configuration
class KafkaTopicBeans {

    @Bean
    fun taskRequestsTopic(): NewTopic {
        return TopicBuilder.name("task_requests")
            .partitions(2)
            .replicas(1)
            .build()
    }

    @Bean
    fun taskResponsesTopic(): NewTopic {
        return TopicBuilder.name("task_responses")
            .partitions(2)
            .replicas(1)
            .build()
    }
}