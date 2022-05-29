package edu.kruchon.natural.lang.parser.codegeneration;

import edu.kruchon.natural.lang.parser.syntax.Parameter;

public class KotlinMethod {
    final String name;
    final String parameterName;

    public KotlinMethod(String name, Parameter parameter) {
        this.name = name;
        this.parameterName = parameter.getName();
    }

    public String getName() {
        return name;
    }

    public String getParameterClassName() {
        return KotlinGenerationUtils.INSTANCE.firstCharToUpperCase(parameterName);
    }

    public String getParameterName() {
        return parameterName;
    }
}
