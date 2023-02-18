package io.github.kruchon.configurator

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary

@TestConfiguration
class TestBeans {
    @Bean
    @Primary
    fun testScenarioProcessorClientStub(): TestScenarioProcessorClientStub {
        return TestScenarioProcessorClientStub()
    }
}