package io.github.kruchon.test.scenario.parser.generation

internal object KotlinGenerationUtils {
    fun firstCharToUpperCase(name: String): String {
        return Character.toUpperCase(name[0]) + name.substring(1)
    }
}