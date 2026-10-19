package com.example.samplecrm.deal;

import java.math.BigDecimal;

/** A deal to create or update. {@code companyId} is required on create only. */
public record DealRequest(String title, BigDecimal amount, Long companyId, Long mainContactId) {}
