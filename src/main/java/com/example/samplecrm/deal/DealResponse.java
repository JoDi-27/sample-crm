package com.example.samplecrm.deal;

import java.math.BigDecimal;

public record DealResponse(long id, String title, BigDecimal amount, Long companyId, Long mainContactId,
        String stage) {

    static DealResponse from(Deal deal) {
        return new DealResponse(deal.getId(), deal.getTitle(), deal.getAmount(), deal.getCompanyId(),
                deal.getMainContactId(), deal.getStage().value());
    }
}
