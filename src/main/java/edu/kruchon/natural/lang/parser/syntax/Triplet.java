package edu.kruchon.natural.lang.parser.syntax;

public class Triplet {
    private final String subject;
    private final String relationship;
    private final Parameter object;

    public Triplet(String subject, String relationship, Parameter object) {
        this.subject = subject;
        this.relationship = relationship;
        this.object = object;
    }

    @Override
    public String toString() {
        return "Triplet{" +
                "subject='" + subject + '\'' +
                ", relationship='" + relationship + '\'' +
                ", object=" + object +
                '}';
    }

    public String getSubject() {
        return subject;
    }

    public String getRelationship() {
        return relationship;
    }

    public Parameter getObject() {
        return object;
    }
}
