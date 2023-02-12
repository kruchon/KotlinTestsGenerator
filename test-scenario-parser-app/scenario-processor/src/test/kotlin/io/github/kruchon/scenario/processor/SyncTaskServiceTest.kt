package io.github.kruchon.scenario.processor

import io.github.kruchon.scenario.processor.task.sync.SyncTaskRequest
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.post

class SyncTaskServiceTest : BaseTest() {

    @Test
    fun `automatic test is generated from scenario`() {
        mockMvc.post("/api/processor/sync/task") {
            content = objectMapper.writeValueAsString(SyncTaskRequest(
                listOf(
                    SyncTaskRequest.Scenario(
                        "TariffTest",
                        "User registered in registration form. User paid simple tariff."
                    )
                )
            ))
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status {
                isOk()
            }
        }
    }

}