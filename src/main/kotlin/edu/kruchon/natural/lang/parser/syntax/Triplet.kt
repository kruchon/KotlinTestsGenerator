package edu.kruchon.natural.lang.parser.syntax

class Triplet(val subject: String, val relationship: String, val `object`: Parameter) {
    @Override
    override fun toString(): String {
        return "Triplet{" +
                "subject='" + subject + '\'' +
                ", relationship='" + relationship + '\'' +
                ", object=" + `object` +
                '}'
    }
}