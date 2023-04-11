package io.github.kruchon.scenario.processor.task

import io.github.kruchon.scenario.configurator.processor.TaskRequest
import io.github.kruchon.scenario.configurator.processor.TaskResponse
import io.github.kruchon.test.scenario.parser.api.TestScenario
import io.github.kruchon.test.scenario.parser.api.TestScenarioParser
import org.springframework.stereotype.Service

@Service
class TaskService {

    fun processRequest(taskRequest: TaskRequest): TaskResponse {
        val testScenarios = taskRequest.scenarios.map { TestScenario(it.name, it.content) }
        val parsingResult = TestScenarioParser.parse(testScenarios, taskRequest.generationPackage, taskRequest.implementationPackage)
        return parsingResult.sources.map { TaskResponse.File(it.name, it.content) }.let { TaskResponse(it, taskRequest.projectId) }
    }

}