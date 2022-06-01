package io.github.kruchon.test.scenario.parser.syntax

internal class Triplet(val subject: String, val relationship: String, val `object`: Parameter) {
    @Override
    override fun toString(): String {
        return "Triplet{" +
                "subject='" + subject + '\'' +
                ", relationship='" + relationship + '\'' +
                ", object=" + `object` +
                '}'
    }
}