package edu.kruchon.natural.lang.parser.generation

import edu.kruchon.natural.lang.parser.syntax.Parameter

internal class KotlinMethod(val name: String, parameter: Parameter) {
    val parameterName = parameter.name
    val parameterClassName = KotlinGenerationUtils.firstCharToUpperCase(parameterName)
}