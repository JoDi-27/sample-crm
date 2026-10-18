package com.example.samplecrm.api;

import jakarta.validation.Validator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * Checks a request body against its Bean Validation constraints. Services add their own checks, such as
 * references to other records, to the returned list before calling {@link BadInputException#throwIfAny}.
 */
@Component
public class RequestValidator {

    private final Validator validator;

    public RequestValidator(Validator validator) {
        this.validator = validator;
    }

    /** Returns a mutable list of the constraint violations of {@code request}, sorted by field. */
    public List<FieldErrorItem> validate(Object request) {
        return validator.validate(request).stream()
                .map(violation -> new FieldErrorItem(violation.getPropertyPath().toString(), violation.getMessage()))
                .sorted(Comparator.comparing(FieldErrorItem::field).thenComparing(FieldErrorItem::message))
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }
}
