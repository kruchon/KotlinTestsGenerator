package io.github.kruchon.test.scenario.parser.generation

import io.github.kruchon.test.scenario.parser.freemarker.TemplateProcessor
import io.github.kruchon.test.scenario.parser.syntax.Parameter
import io.github.kruchon.test.scenario.parser.syntax.Triplet

internal object KotlinSourceGenerator {
    internal fun generateSingleScenario(triplets: List<Triplet>, testScenarioName: String): TestScenarioParsingResult {
        val sources = generateSubjectInterfaces(triplets) +
                generateParameterDataClasses(triplets) +
                generateSingleAutomatedTest(triplets)
        return TestScenarioParsingResult(sources)
    }

    // todo generation of multiple tests
    private fun generateSingleAutomatedTest(triplets: List<Triplet>): Set<KotlinSource> {
        val functionCalls = triplets.map { generateFunctionCall(it) }
        return setOf(generateAutomatedTest(functionCalls))
    }

    private fun generateFunctionCall(triplet: Triplet): KotlinFunctionCall {
        val constructorCall = createConstructorCallsRecursively(triplet.`object`, 0)
        val functionName = getFunctionName(triplet.relationship)
        return KotlinFunctionCall(triplet.subject.lowercase(), functionName, constructorCall)
    }

    private fun createConstructorCallsRecursively(parameter: Parameter, nestedLevel: Int): KotlinConstructorCall {
        val childrenParameters = parameter.childrenParameters
        val parameterClassName = KotlinGenerationUtils.firstCharToUpperCase(parameter.name)
        nestedLevel.inc()
        val childrenConstructorCalls = childrenParameters.map { childrenParameter ->
            createConstructorCallsRecursively(
                    childrenParameter,
                    nestedLevel
            )
        }
        return KotlinConstructorCall(nestedLevel, parameterClassName, childrenConstructorCalls, parameter.values)
    }

    private fun generateAutomatedTest(functionCalls: List<KotlinFunctionCall>): KotlinSource {
        val subjects = functionCalls
                .map { it.contextObject }
                .map { KotlinGenerationUtils.firstCharToUpperCase(it) }
                .distinct()
        val templateParameters = mutableMapOf<String, Any>()
        templateParameters["subjects"] = subjects
        templateParameters["functionCalls"] = functionCalls
        templateParameters["constructorCallNames"] = functionCalls
                .flatMap {
                    val constructorCallNames = mutableSetOf<String>()
                    addAllChildrenConstructorCallNamesRecursively(constructorCallNames, it.constructorCall)
                    constructorCallNames
                }
                .distinct()
        val content = TemplateProcessor.process("Test.kt", templateParameters)
        return KotlinSource("Test.kt", content)
    }

    private fun addAllChildrenConstructorCallNamesRecursively(
            collectedConstructorCalls: MutableSet<String>,
            constructorCall: KotlinConstructorCall
    ) {
        collectedConstructorCalls.add(constructorCall.name)
        for (childrenConstructorCall in constructorCall.childrenConstructorCalls) {
            addAllChildrenConstructorCallNamesRecursively(collectedConstructorCalls, childrenConstructorCall)
        }
    }

    private fun generateParameterDataClasses(triplets: List<Triplet>): Set<KotlinSource> {
        return triplets
                .flatMap {
                    val collectedChildrenParameters = mutableSetOf<Parameter>()
                    collectedChildrenParameters.add(it.`object`)
                    addAllChildrenParameters(collectedChildrenParameters, it.`object`)
                    collectedChildrenParameters
                }
                .distinct()
                .map { generateParameterDataClass(it) }
                .toSet()
    }

    private fun addAllChildrenParameters(collectedChildrenParameters: MutableSet<Parameter>, parameter: Parameter) {
        val childrenParameters = parameter.childrenParameters
        if (childrenParameters.isEmpty()) {
            return
        }
        collectedChildrenParameters.addAll(childrenParameters)
        for (childrenParameter in childrenParameters) {
            addAllChildrenParameters(collectedChildrenParameters, childrenParameter)
        }
    }

    private fun generateParameterDataClass(parameter: Parameter): KotlinSource {
        val templateParameters = mutableMapOf<String, Any>()
        val fieldClasses = mutableSetOf<KotlinParameterClass>()
        for (field in parameter.childrenParameters) {
            fieldClasses.add(
                    KotlinParameterClass(
                            KotlinGenerationUtils.firstCharToUpperCase(field.name),
                            emptySet(),
                            emptyList()
                    )
            )
        }
        val valueTypes = parameter.values.map { ValueType.STRING }
        val parameterClass =
                KotlinParameterClass(KotlinGenerationUtils.firstCharToUpperCase(parameter.name), fieldClasses, valueTypes)
        templateParameters["parameterClass"] = parameterClass
        val content = TemplateProcessor.process("Parameter.kt", templateParameters)
        return KotlinSource(parameterClass.name + ".kt", content)
    }

    private fun generateSubjectInterfaces(triplets: List<Triplet>): Set<KotlinSource> {
        val subjectKotlinMethodMappings = mutableMapOf<String, MutableSet<KotlinMethod>>()
        triplets.forEach { triplet ->
            val functionName = getFunctionName(triplet.relationship)
            val kotlinMethod = KotlinMethod(functionName, triplet.`object`)
            val subject = triplet.subject
            subjectKotlinMethodMappings.putIfAbsent(subject, HashSet())
            checkNotNull(subjectKotlinMethodMappings[subject]).add(kotlinMethod)
        }
        return subjectKotlinMethodMappings.entries.map { generateSubjectInterface(it) }.toSet()
    }

    private fun getFunctionName(relationship: String): String {
        return if (relationship.split(" ").size > 1) {
            "`$relationship`"
        } else {
            relationship
        }
    }

    private fun generateSubjectInterface(subjectWithKotlinMethods: Map.Entry<String, Set<KotlinMethod>>): KotlinSource {
        val subjectClassName = KotlinGenerationUtils.firstCharToUpperCase(subjectWithKotlinMethods.key)
        val kotlinMethods = subjectWithKotlinMethods.value
        val templateParameters = mutableMapOf<String, Any>()
        templateParameters["subjectClass"] = subjectClassName
        templateParameters["kotlinMethods"] = kotlinMethods
        templateParameters["parameterClassNames"] = kotlinMethods.map { it.parameterClassName }.distinct()
        val content = TemplateProcessor.process("Subject.kt", templateParameters)
        return KotlinSource("$subjectClassName.kt", content)
    }
}