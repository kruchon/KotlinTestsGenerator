package io.github.kruchon.test.scenario.parser.generation

internal class KotlinGenerationProperties(
    private val generationPackage: String,
    private val implementationPackage: String
) {
    fun addToTemplateParameters(templateParameters: MutableMap<String, Any>) {
        templateParameters["generationPackage"] = generationPackage
        templateParameters["implementationPackage"] = implementationPackage
    }
}