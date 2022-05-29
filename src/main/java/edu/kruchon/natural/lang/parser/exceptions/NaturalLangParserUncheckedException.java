package edu.kruchon.natural.lang.parser.exceptions;

public class NaturalLangParserUncheckedException extends RuntimeException {

    public NaturalLangParserUncheckedException() {
    }

    public NaturalLangParserUncheckedException(String message) {
        super(message);
    }

    public NaturalLangParserUncheckedException(String message, Throwable cause) {
        super(message, cause);
    }

    public NaturalLangParserUncheckedException(Throwable cause) {
        super(cause);
    }

    public NaturalLangParserUncheckedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
