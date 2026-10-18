package com.example.samplecrm.contact;

public record ContactResponse(long id, String firstName, String lastName, String email, String phone,
        Long companyId) {

    static ContactResponse from(Contact contact) {
        return new ContactResponse(contact.getId(), contact.getFirstName(), contact.getLastName(),
                contact.getEmail(), contact.getPhone(), contact.getCompanyId());
    }
}
