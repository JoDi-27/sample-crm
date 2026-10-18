package com.example.samplecrm.deal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

/** A deal to create or update. {@code companyId} is required on create only; the service checks that. */
public record DealRequest(
        @NotBlank @Size(max = 200) String title,
        @NotNull @DecimalMin("0") @Digits(integer = 13, fraction = 2) BigDecimal amount,
        Long companyId,
        Long mainContactId) {}
