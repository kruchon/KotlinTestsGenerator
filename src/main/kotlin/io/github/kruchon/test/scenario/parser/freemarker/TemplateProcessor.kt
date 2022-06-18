package io.github.kruchon.test.scenario.parser.freemarker

import freemarker.template.Configuration
import freemarker.template.TemplateException
import freemarker.template.TemplateExceptionHandler
import io.github.kruchon.test.scenario.parser.exceptions.NaturalLangParserUncheckedException
import io.github.kruchon.test.scenario.parser.generation.KotlinGenerationProperties
import java.io.File
import java.io.IOException
import java.io.StringWriter

internal object TemplateProcessor {

    private val cfg: Configuration = Configuration(Configuration.VERSION_2_3_20).apply {
        try {
            this.setDirectoryForTemplateLoading(File(checkNotNull(this.javaClass.classLoader.getResource("./templates")).file))
            this.defaultEncoding = "UTF-8"
            this.templateExceptionHandler = TemplateExceptionHandler.RETHROW_HANDLER
            this.logTemplateExceptions = false
            this.wrapUncheckedExceptions = true
            this.fallbackOnNullLoopVariable = false
        } catch (e: IOException) {
            throw NaturalLangParserUncheckedException(e)
        }
    }

    fun process(templateName: String, templateParameters: Map<String, Any>): String {
        try {
            StringWriter().use { out ->
                val implementationPackage = KotlinGenerationProperties.implementationPackage
                        ?: throw NaturalLangParserUncheckedException("Implementation package is not defined in object KotlinGenerationProperties")
                val commonTemplateParameters = mapOf(
                        "generationPackage" to KotlinGenerationProperties.generationPackage,
                        "implementationPackage" to implementationPackage
                )
                val template = cfg.getTemplate(templateName)
                template.process(
                        templateParameters + commonTemplateParameters, out
                )
                return out.toString()
            }
        } catch (e: IOException) {
            throw NaturalLangParserUncheckedException("Cannot process template $templateName", e)
        } catch (e: TemplateException) {
            throw NaturalLangParserUncheckedException("Cannot process template $templateName", e)
        }
    }
}