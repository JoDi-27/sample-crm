package com.example.samplecrm.api;

import java.util.List;

/** The request is invalid. Carries every problem found, so the client can fix them all at once. */
public class BadInputException extends RuntimeException {

    private final List<FieldErrorItem> errors;

    public BadInputException(List<FieldErrorItem> errors) {
        super("The request has invalid fields: " + errors);
        this.errors = List.copyOf(errors);
    }

    public List<FieldErrorItem> errors() {
        return errors;
    }

    /** Throws when {@code errors} is not empty. */
    public static void throwIfAny(List<FieldErrorItem> errors) {
        if (!errors.isEmpty()) {
            throw new BadInputException(errors);
        }
    }
}
