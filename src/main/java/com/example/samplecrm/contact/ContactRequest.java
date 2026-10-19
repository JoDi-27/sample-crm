package com.example.samplecrm.contact;

public record ContactRequest(String firstName, String lastName, String email, String phone, Long companyId) {}
