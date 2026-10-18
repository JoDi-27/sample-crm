package com.example.samplecrm.contact;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "contacts")
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String firstName;

    @Column(nullable = false, length = 100)
    private String lastName;

    @Column(nullable = false, length = 254)
    private String email;

    @Column(length = 50)
    private String phone;

    /** Cleared by the database when the company is deleted. */
    private Long companyId;

    protected Contact() {}

    Contact(ContactRequest request) {
        update(request);
    }

    void update(ContactRequest request) {
        this.firstName = request.firstName();
        this.lastName = request.lastName();
        this.email = request.email();
        this.phone = request.phone();
        this.companyId = request.companyId();
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public Long getCompanyId() {
        return companyId;
    }
}
