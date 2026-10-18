package com.example.samplecrm.api;

/** One problem with a request: the field or parameter that is wrong, and why. */
public record FieldErrorItem(String field, String message) {}
