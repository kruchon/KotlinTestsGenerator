package edu.kruchon.natural.lang.parser.syntax

class Parameter(val name: String, val values: List<String>, val childrenParameters: List<Parameter>) {
    @Override
    override fun toString(): String {
        return "Parameter{" +
                "name='" + name + '\'' +
                ", values=" + values +
                ", childrenParameters=" + childrenParameters +
                '}'
    }

    override fun equals(other: Any?): kotlin.Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Parameter

        if (name != other.name) return false
        if (values != other.values) return false

        return true
    }

    override fun hashCode(): kotlin.Int {
        var result = name.hashCode()
        result = 31 * result + values.hashCode()
        return result
    }


}