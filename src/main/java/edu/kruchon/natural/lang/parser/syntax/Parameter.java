package edu.kruchon.natural.lang.parser.syntax;

import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Parameter {
    private final String name;
    private final List<String> values;
    private final List<Parameter> childrenParameters;

    public Parameter(String name, List<String> values, List<Parameter> childrenParameters) {
        this.name = name;
        this.values = values;
        this.childrenParameters = childrenParameters;
    }

    @Override
    public String toString() {
        return "Parameter{" +
                "name='" + name + '\'' +
                ", values=" + values +
                ", childrenParameters=" + childrenParameters +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Parameter parameter = (Parameter) o;
        return Objects.equals(name, parameter.name) &&
                Objects.equals(values, parameter.values);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, values);
    }

    public String getName() {
        return name;
    }

    public List<String> getValues() {
        return values;
    }

    public List<Parameter> getChildrenParameters() {
        return childrenParameters;
    }
}
