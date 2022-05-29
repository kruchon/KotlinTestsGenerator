package edu.kruchon.natural.lang.parser.codegeneration;

import java.util.List;

public class KotlinConstructorCall {
    private final String name;
    private final List<KotlinConstructorCall> childrenConstructorCalls;
    private final List<String> values;
    private final int nestedLevel;

    public KotlinConstructorCall(int nestedLevel, String name, List<KotlinConstructorCall> childrenConstructorCalls, List<String> values) {
        this.nestedLevel = nestedLevel;
        this.childrenConstructorCalls = childrenConstructorCalls;
        this.values = values;
        this.name = name;
    }

    public List<KotlinConstructorCall> getChildrenConstructorCalls() {
        return childrenConstructorCalls;
    }

    public List<String> getValues() {
        return values;
    }

    public String getName() {
        return name;
    }

    public int getNestedLevel() {
        return nestedLevel;
    }
}
