package edu.kruchon.natural.lang.parser.codegeneration

import edu.kruchon.natural.lang.parser.syntax.Parameter

class KotlinMethod(val name: String, parameter: Parameter) {
    val parameterName: String
    val parameterClassName: String

    init {
        parameterName = parameter.name
        parameterClassName = KotlinGenerationUtils.firstCharToUpperCase(parameterName)
    }
}