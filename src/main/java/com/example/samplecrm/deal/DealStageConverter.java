package com.example.samplecrm.deal;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/** Stores a {@link DealStage} as its lower-case text, the values the {@code deals.stage} check allows. */
@Converter(autoApply = true)
class DealStageConverter implements AttributeConverter<DealStage, String> {

    @Override
    public String convertToDatabaseColumn(DealStage stage) {
        return stage == null ? null : stage.value();
    }

    @Override
    public DealStage convertToEntityAttribute(String text) {
        return text == null ? null : DealStage.parse(text)
                .orElseThrow(() -> new IllegalStateException("Unknown deal stage in the database: " + text));
    }
}
