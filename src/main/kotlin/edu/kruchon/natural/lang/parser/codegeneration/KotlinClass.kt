package edu.kruchon.natural.lang.parser.codegeneration

class KotlinClass {
    val name: String

    // todo take in account the case with duplicated field names
    val fields: Set<KotlinClass>

    constructor(name: String) {
        this.name = name
        fields = setOf()
    }

    constructor(name: String, fields: Set<KotlinClass>) {
        this.name = name
        this.fields = fields
    }
}