package com.example.samplecrm.api;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.beans.TypeMismatchException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import tools.jackson.core.JacksonException;

/**
 * Turns errors into RFC 9457 problem details. Bad input is a 400 with an {@code errors} list naming each wrong
 * field or parameter; a missing record is a 404.
 */
@RestControllerAdvice
class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String INVALID_FIELDS = "The request has invalid fields.";

    @ExceptionHandler
    ProblemDetail badInput(BadInputException ex) {
        return badRequest(ex.errors());
    }

    @ExceptionHandler
    ProblemDetail notFound(NotFoundException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    /** A referenced record was deleted between the existence check and the write. */
    @ExceptionHandler
    ProblemDetail dataIntegrity(DataIntegrityViolationException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
                "The request refers to a record that does not exist.");
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
                "The request body is not valid JSON for this operation.");
        String field = jsonPath(ex);
        if (field != null) {
            problem.setDetail(INVALID_FIELDS);
            problem.setProperty("errors", List.of(new FieldErrorItem(field, "has a value of the wrong type")));
        }
        return ResponseEntity.badRequest().body(problem);
    }

    @Override
    protected ResponseEntity<Object> handleHandlerMethodValidationException(HandlerMethodValidationException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        List<FieldErrorItem> errors = ex.getParameterValidationResults().stream()
                .flatMap(result -> result.getResolvableErrors().stream()
                        .map(error -> new FieldErrorItem(result.getMethodParameter().getParameterName(),
                                error.getDefaultMessage())))
                .sorted(Comparator.comparing(FieldErrorItem::field))
                .toList();
        return ResponseEntity.badRequest().body(badRequest(errors));
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers,
            HttpStatusCode status, WebRequest request) {
        String name = ex instanceof MethodArgumentTypeMismatchException mismatch ? mismatch.getName()
                : ex.getPropertyName();
        return ResponseEntity.badRequest()
                .body(badRequest(List.of(new FieldErrorItem(name, "has a value of the wrong type"))));
    }

    private static ProblemDetail badRequest(List<FieldErrorItem> errors) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, INVALID_FIELDS);
        problem.setProperty("errors", errors);
        return problem;
    }

    /** The JSON property path where reading failed, such as {@code amount}, or null when unknown. */
    private static String jsonPath(HttpMessageNotReadableException ex) {
        if (!(ex.getCause() instanceof JacksonException jackson) || jackson.getPath().isEmpty()) {
            return null;
        }
        String path = jackson.getPath().stream()
                .map(JacksonException.Reference::getPropertyName)
                .filter(Objects::nonNull)
                .collect(Collectors.joining("."));
        return path.isEmpty() ? null : path;
    }
}
