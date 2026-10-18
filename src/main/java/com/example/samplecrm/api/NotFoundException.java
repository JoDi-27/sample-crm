package com.example.samplecrm.api;

/** The record the request names does not exist. */
public class NotFoundException extends RuntimeException {

    public NotFoundException(String type, long id) {
        super(type + " " + id + " was not found");
    }
}
