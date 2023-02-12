package io.github.kruchon.scenario.processor

import io.github.kruchon.scenario.processor.task.sync.SyncTaskRequest
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.post

class SyncTaskServiceTest : BaseTest() {

    @Test
    fun `automatic test is generated from scenario`() {
        mockMvc.post("/api/processor/sync/task") {
            content = objectMapper.writeValueAsString(
                SyncTaskRequest(
                    listOf(
                        SyncTaskRequest.Scenario(
                            "TariffTest",
                            "User registered in registration form. User paid simple tariff."
                        )
                    ),
                    generationPackage = "io.github.kruchon",
                    implementationPackage = "test.package"
                )
            )
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status {
                isOk()
            }
            content {
                json(
                    //language=json
                    """
                    {
                       "files":[
                          {
                             "name":"User.kt",
                             "content":"package todo.override.default.generation.package\r\n\r\nimport todo.override.default.generation.package.Form\nimport todo.override.default.generation.package.Tariff\r\n\r\ninterface User {\r\n    infix fun `register in`(form: Form)\r\n    infix fun pay(tariff: Tariff)\r\n}"
                          },
                          {
                             "name":"Form.kt",
                             "content":"package todo.override.default.generation.package\r\n\r\ndata class Form(\r\n    val value: String\r\n)"
                          },
                          {
                             "name":"Tariff.kt",
                             "content":"package todo.override.default.generation.package\r\n\r\ndata class Tariff(\r\n    val value: String\r\n)"
                          },
                          {
                             "name":"TariffTest.kt",
                             "content":"package todo.override.default.generation.package\r\n\r\nimport todo.override.default.implementation.package.UserImpl\r\nimport todo.override.default.generation.package.Form\r\nimport todo.override.default.generation.package.Tariff\r\n\r\nclass Test {\r\n    @Test\r\n    fun test() {\r\n        val user = UserImpl()\r\n        user `register in` Form(value = \"registration\")\r\n        user pay Tariff(value = \"simple\")\r\n    }\r\n}"
                          }
                       ]
                    }
                        """
                )
            }
        }
    }

}