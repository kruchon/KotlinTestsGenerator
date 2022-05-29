package edu.kruchon.natural.lang.parser.freemarker;

import edu.kruchon.natural.lang.parser.exceptions.NaturalLangParserUncheckedException;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;
import java.util.Objects;

public enum TemplateProcessor {
    INSTANCE;

    private Configuration cfg;

    TemplateProcessor() {
        try {
            cfg = new Configuration(Configuration.VERSION_2_3_20);
            cfg.setDirectoryForTemplateLoading(new File(Objects.requireNonNull(this.getClass().getClassLoader().getResource("./templates")).getFile()));
            cfg.setDefaultEncoding("UTF-8");
            cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
            cfg.setLogTemplateExceptions(false);
            cfg.setWrapUncheckedExceptions(true);
            cfg.setFallbackOnNullLoopVariable(false);
        } catch (IOException e) {
            throw new NaturalLangParserUncheckedException(e);
        }
    }

    public String process(String templateName, Map<String, Object> templateParameters) {
        try (Writer out = new StringWriter()) {
            Template template = cfg.getTemplate(templateName);
            template.process(templateParameters, out);
            return out.toString();
        } catch (IOException | TemplateException e) {
            throw new NaturalLangParserUncheckedException("Cannot process template " + templateName, e);
        }
    }
}