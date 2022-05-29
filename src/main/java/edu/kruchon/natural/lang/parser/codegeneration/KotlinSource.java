package edu.kruchon.natural.lang.parser.codegeneration;

public class KotlinSource {
    private final String name;
    private final String content;

    public KotlinSource(String name, String content) {
        this.name = name;
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return "KotlinSource{" +
                "name='" + name + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
