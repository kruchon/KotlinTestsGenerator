package io.github.kruchon.scenario.configurator

import java.util.Collections
import org.springframework.boot.SpringApplication
import org.springframework.boot.env.EnvironmentPostProcessor
import org.springframework.core.Ordered
import org.springframework.core.Ordered.LOWEST_PRECEDENCE
import org.springframework.core.env.ConfigurableEnvironment
import org.springframework.core.env.MapPropertySource
import org.testcontainers.containers.PostgreSQLContainer

class TestEnvironmentPostProcessor : EnvironmentPostProcessor, Ordered {
    override fun postProcessEnvironment(environment: ConfigurableEnvironment, application: SpringApplication) {
        environment.propertySources.addFirst(
            MapPropertySource(
                "dockerPostgresInfo",
                mapOf(
                    "postgres-container.jdbcUrl" to postgresContainer.jdbcUrl,
                    "postgres-container.username" to postgresContainer.username,
                    "postgres-container.password" to postgresContainer.password,
                    "postgres-container.driverClassName" to postgresContainer.driverClassName
                )
            )
        )
    }

    companion object {
        class KPostgreSQLContainer(imageName: String) : PostgreSQLContainer<KPostgreSQLContainer>(imageName)

        val postgresContainer: KPostgreSQLContainer = KPostgreSQLContainer("postgres:15.2")
            .withDatabaseName("scenario_processor")
            .withUsername("postgres")
            .withPassword("postgres")
            .withTmpFs(Collections.singletonMap("/var/lib/postgresql", "rw"))
            .withCommand("postgres -c max_connections=2000 -c fsync=off")
            .apply {
                start()
                Runtime.getRuntime().addShutdownHook(
                    Thread {
                        stop()
                    }
                )
            }
    }

    override fun getOrder(): Int {
        return LOWEST_PRECEDENCE - 1
    }
}
