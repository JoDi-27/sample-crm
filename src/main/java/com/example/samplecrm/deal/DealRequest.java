package com.example.samplecrm.deal;

import java.math.BigDecimal;

/**
 * A deal to create or update. {@code companyId} is required on create only. {@code stage} is optional text, checked
 * by the controller: on create it defaults to open, and on update a missing stage keeps the current one.
 */
public record DealRequest(String title, BigDecimal amount, Long companyId, Long mainContactId, String stage) {}
