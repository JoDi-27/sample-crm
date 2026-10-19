package com.example.samplecrm.company;

public record CompanyResponse(long id, String name, String website) {

    static CompanyResponse from(Company company) {
        return new CompanyResponse(company.getId(), company.getName(), company.getWebsite());
    }
}
