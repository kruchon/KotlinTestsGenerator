package io.github.kruchon.test.scenario.parser.freemarker

import freemarker.template.Configuration
import freemarker.template.TemplateException
import freemarker.template.TemplateExceptionHandler
import io.github.kruchon.test.scenario.parser.exceptions.NaturalLangParserUncheckedException
import io.github.kruchon.test.scenario.parser.generation.KotlinGenerationProperties
import java.io.IOException
import java.io.StringWriter

internal object TemplateProcessor {

    private val cfg: Configuration = Configuration(Configuration.VERSION_2_3_20).apply {
        try {
            this.setClassForTemplateLoading(TemplateProcessor::class.java, "/templates");
            this.defaultEncoding = "UTF-8"
            this.templateExceptionHandler = TemplateExceptionHandler.RETHROW_HANDLER
            this.logTemplateExceptions = false
            this.wrapUncheckedExceptions = true
            this.fallbackOnNullLoopVariable = false
        } catch (e: IOException) {
            throw NaturalLangParserUncheckedException(e)
        }
    }

    fun process(
        templateName: String,
        templateParameters: MutableMap<String, Any>,
        kotlinGenerationProperties: KotlinGenerationProperties
    ): String {
        kotlinGenerationProperties.addToTemplateParameters(templateParameters)
        try {
            StringWriter().use { out ->
                val template = cfg.getTemplate(templateName)
                template.process(
                    templateParameters, out
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