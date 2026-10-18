package com.example.samplecrm.deal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "deals")
public class Deal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    /** Cleared by the database when the company is deleted. */
    private Long companyId;

    /** Cleared by the database when the contact is deleted. */
    private Long mainContactId;

    protected Deal() {}

    Deal(DealRequest request) {
        update(request);
    }

    void update(DealRequest request) {
        this.title = request.title();
        this.amount = request.amount();
        this.companyId = request.companyId();
        this.mainContactId = request.mainContactId();
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public Long getMainContactId() {
        return mainContactId;
    }
}
