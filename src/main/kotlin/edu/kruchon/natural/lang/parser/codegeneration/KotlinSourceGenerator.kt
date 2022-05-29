package edu.kruchon.natural.lang.parser.codegeneration

import edu.kruchon.natural.lang.parser.freemarker.TemplateProcessor
import edu.kruchon.natural.lang.parser.syntax.Parameter
import edu.kruchon.natural.lang.parser.syntax.Triplet

object KotlinSourceGenerator {
    fun generate(triplets: List<Triplet>): List<KotlinSource> {
        return generateSubjectInterfaces(triplets) + generateParameterDataClasses(triplets) + generateAutomaticTests(triplets)
    }

    // todo generation of multiple tests
    private fun generateAutomaticTests(triplets: List<Triplet>): List<KotlinSource> {
        val functionCalls = triplets.map { generateFunctionCall(it) }
        return listOf(generateAutomaticTest(functionCalls))
    }

    private fun generateFunctionCall(triplet: Triplet): KotlinFunctionCall {
        val `object` = triplet.`object`
        val constructorCall = createConstructorCallsRecursively(`object`, 0)
        return KotlinFunctionCall(triplet.subject.lowercase(), triplet.relationship, constructorCall)
    }

    private fun createConstructorCallsRecursively(parameter: Parameter, nestedLevel: Int): KotlinConstructorCall {
        val childrenParameters = parameter.childrenParameters
        val parameterClassName = KotlinGenerationUtils.firstCharToUpperCase(parameter.name)
        nestedLevel.inc()
        return if (childrenParameters.isEmpty()) {
            KotlinConstructorCall(nestedLevel, parameterClassName, emptyList(), parameter.values)
        } else {
            val childrenConstructorCalls = childrenParameters.map { childrenParameter -> createConstructorCallsRecursively(childrenParameter, nestedLevel) }
            KotlinConstructorCall(nestedLevel, parameterClassName, childrenConstructorCalls, emptyList())
        }
    }

    private fun generateAutomaticTest(functionCalls: List<KotlinFunctionCall>): KotlinSource {
        val subjects = functionCalls.map { it.contextObject }.map { KotlinGenerationUtils.firstCharToUpperCase(it) }.distinct()
        val templateParameters = mutableMapOf<String, Any>()
        templateParameters["subjects"] = subjects
        templateParameters["functionCalls"] = functionCalls
        val content = TemplateProcessor.process("Test.kt", templateParameters)
        return KotlinSource("Test.kt", content)
    }

    private fun generateParameterDataClasses(triplets: List<Triplet>): List<KotlinSource> {
        return triplets.flatMap {
            val collectedChildrenParameters = mutableSetOf<Parameter>()
            val `object` = it.`object`
            collectedChildrenParameters.add(`object`)
            addAllChildrenParameters(collectedChildrenParameters, `object`)
            collectedChildrenParameters
        }.distinct().map { generateParameterDataClass(it) }
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
        val fieldClasses = mutableSetOf<KotlinClass>()
        for (field in parameter.childrenParameters) {
            fieldClasses.add(KotlinClass(KotlinGenerationUtils.firstCharToUpperCase(field.name)))
        }
        val parameterClass = KotlinClass(KotlinGenerationUtils.firstCharToUpperCase(parameter.name), fieldClasses)
        templateParameters["parameterClass"] = parameterClass
        val content = TemplateProcessor.process("Parameter.kt", templateParameters)
        return KotlinSource(parameterClass.name.toString() + ".kt", content)
    }

    private fun generateSubjectInterfaces(triplets: List<Triplet>): List<KotlinSource> {
        val subjectKotlinMethodMappings = mutableMapOf<String, MutableSet<KotlinMethod>>()
        triplets.forEach { triplet ->
            val kotlinMethod = KotlinMethod(triplet.relationship, triplet.`object`)
            val subject = triplet.subject
            subjectKotlinMethodMappings.putIfAbsent(subject, HashSet())
            checkNotNull(subjectKotlinMethodMappings[subject]).add(kotlinMethod)
        }
        return subjectKotlinMethodMappings.entries.map { generateSubjectInterface(it) }
    }

    private fun generateSubjectInterface(subjectWithKotlinMethods: Map.Entry<String, Set<KotlinMethod>>): KotlinSource {
        val subjectClassName = KotlinGenerationUtils.firstCharToUpperCase(subjectWithKotlinMethods.key)
        val kotlinMethods = subjectWithKotlinMethods.value
        val templateParameters = mutableMapOf<String, Any>()
        templateParameters["subjectClass"] = subjectClassName
        templateParameters["kotlinMethods"] = kotlinMethods
        val content = TemplateProcessor.process("Subject.kt", templateParameters)
        return KotlinSource("$subjectClassName.kt", content)
    }
}