package io.github.kruchon.test.scenario.parser.generation

import io.github.kruchon.test.scenario.parser.syntax.Parameter

internal class KotlinMethod(val name: String, parameter: Parameter) {
    val parameterName = parameter.name
    val parameterClassName = KotlinGenerationUtils.firstCharToUpperCase(parameterName)
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as KotlinMethod

        if (name != other.name) return false
        if (parameterName != other.parameterName) return false
        if (parameterClassName != other.parameterClassName) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + parameterName.hashCode()
        result = 31 * result + parameterClassName.hashCode()
        return result
    }


}