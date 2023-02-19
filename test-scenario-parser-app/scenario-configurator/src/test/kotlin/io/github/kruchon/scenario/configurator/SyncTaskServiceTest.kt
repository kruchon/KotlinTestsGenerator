package io.github.kruchon.scenario.configurator

import io.github.kruchon.scenario.configurator.project.CreateProjectParameters
import io.github.kruchon.scenario.configurator.project.view.ProjectView
import io.github.kruchon.scenario.configurator.scenario.CreateScenarioParameters
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

class SyncTaskServiceTest : BaseTest() {

    @Autowired
    lateinit var testScenarioProcessorClientStub: TestScenarioProcessorClientStub

    @Test
    fun `automatic test is generated from scenario`() {
        var project = objectMapper.readValue(mockMvc.post("/api/configurator/project") {
            content = objectMapper.writeValueAsString(
                CreateProjectParameters(
                    "test",
                    "test",
                    "test"
                )
            )
            contentType = MediaType.APPLICATION_JSON
        }.andReturn().response.contentAsString, ProjectView::class.java)

        mockMvc.post("/api/configurator/project/${project.id}/scenario") {
            content = objectMapper.writeValueAsString(
                CreateScenarioParameters(
                    "TestOne",
                    "User paid simple tariff."
                )
            )
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status {
                isOk()
            }
        }

        mockMvc.post("/api/configurator/project/${project.id}/scenario") {
            content = objectMapper.writeValueAsString(
                CreateScenarioParameters(
                    "TestTwo",
                    "User paid another tariff."
                )
            )
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status {
                isOk()
            }
        }

        project = objectMapper.readValue(
            mockMvc.get("/api/configurator/project/${project.id}")
                .andReturn().response.contentAsString, ProjectView::class.java
        )
        assertEquals(2, project.scenarios.size)
        assertEquals(0, testScenarioProcessorClientStub.getRequestedProjects().size)
        assertEquals(0, project.sources.size)

        mockMvc.post("/api/configurator/project/${project.id}/process").andExpect {
            status {
                isOk()
            }
        }

        project = objectMapper.readValue(
            mockMvc.get("/api/configurator/project/${project.id}")
                .andReturn().response.contentAsString, ProjectView::class.java
        )
        assertEquals(2, project.scenarios.size)
        assertEquals(1, testScenarioProcessorClientStub.getRequestedProjects().size)
        assertEquals(2, project.sources.size)
    }

}