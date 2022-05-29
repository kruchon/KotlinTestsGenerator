package edu.kruchon.natural.lang.parser.codegeneration;

public class KotlinFunctionCall {
    private final String contextObject;
    private final String name;
    private final KotlinConstructorCall constructorCall;

    public KotlinFunctionCall(String contextObject, String name, KotlinConstructorCall constructorCall) {
        this.name = name;
        this.constructorCall = constructorCall;
        this.contextObject = contextObject;
    }

    public String getName() {
        return name;
    }

    public KotlinConstructorCall getConstructorCall() {
        return constructorCall;
    }

    public String getContextObject() {
        return contextObject;
    }
}
