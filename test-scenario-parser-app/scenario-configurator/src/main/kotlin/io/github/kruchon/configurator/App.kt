package io.github.kruchon.configurator

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.transaction.annotation.EnableTransactionManagement

@SpringBootApplication
class App

fun main(args: Array<String>) {
    runApplication<App>(*args)
}