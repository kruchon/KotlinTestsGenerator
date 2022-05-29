package edu.kruchon.natural.lang.parser.codegeneration;

public enum KotlinGenerationUtils {
    INSTANCE;

    public String firstCharToUpperCase(String name) {
        return Character.toUpperCase(name.charAt(0)) + name.substring(1);
    }
}
