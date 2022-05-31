package io.github.test.scenario.parser.generation

class KotlinSource(val name: String, val content: String) {
    @Override
    override fun toString(): String {
        return "KotlinSource{" +
                "name='" + name + '\'' +
                ", content='" + content + '\'' +
                '}'
    }
}