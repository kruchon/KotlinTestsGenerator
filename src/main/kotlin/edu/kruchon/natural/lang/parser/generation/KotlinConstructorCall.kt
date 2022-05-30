package edu.kruchon.natural.lang.parser.generation

internal class KotlinConstructorCall(val nestedLevel: Int, val name: String, val childrenConstructorCalls: List<KotlinConstructorCall>, val values: List<String>)