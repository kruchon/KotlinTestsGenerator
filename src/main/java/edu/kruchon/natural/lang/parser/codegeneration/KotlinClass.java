package edu.kruchon.natural.lang.parser.codegeneration;

import java.util.HashSet;
import java.util.Set;

public class KotlinClass {
    private final String name;
    // todo take in account the case with duplicated field names
    private final Set<KotlinClass> fields;

    public KotlinClass(String name) {
        this.name = name;
        this.fields = new HashSet<>();
    }

    public KotlinClass(String name, Set<KotlinClass> fields) {
        this.name = name;
        this.fields = new HashSet<>(fields);
    }

    public String getName() {
        return name;
    }

    public Set<KotlinClass> getFields() {
        return fields;
    }
}
