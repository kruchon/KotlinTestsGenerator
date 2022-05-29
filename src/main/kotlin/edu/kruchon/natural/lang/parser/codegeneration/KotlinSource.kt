package edu.kruchon.natural.lang.parser.codegeneration

class KotlinSource(val name: String, val content: String) {
    @Override
    override fun toString(): String {
        return "KotlinSource{" +
                "name='" + name + '\'' +
                ", content='" + content + '\'' +
                '}'
    }
}