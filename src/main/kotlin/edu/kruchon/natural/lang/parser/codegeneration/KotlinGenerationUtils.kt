package edu.kruchon.natural.lang.parser.codegeneration

object KotlinGenerationUtils {
    fun firstCharToUpperCase(name: String): String {
        return Character.toUpperCase(name[0]) + name.substring(1)
    }
}