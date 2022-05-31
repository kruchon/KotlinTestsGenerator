package io.github.test.scenario.parser.generation

import io.github.test.scenario.parser.syntax.Parameter

internal class KotlinMethod(val name: String, parameter: Parameter) {
    val parameterName = parameter.name
    val parameterClassName = KotlinGenerationUtils.firstCharToUpperCase(parameterName)
}