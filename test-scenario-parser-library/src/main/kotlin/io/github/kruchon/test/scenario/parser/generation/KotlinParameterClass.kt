package io.github.kruchon.test.scenario.parser.generation

internal class KotlinParameterClass(
    val name: String,
    // todo take in account the case with duplicated field names
    val fields: Set<KotlinParameterClass>,
    // todo take in account different parameter types, for now strings only
    val valueTypes: List<ValueType>
)

enum class ValueType {
    STRING
}