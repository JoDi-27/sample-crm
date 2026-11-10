package com.example.samplecrm.deal;

import java.util.Arrays;
import java.util.Optional;

/** Where a deal stands: still open, or its outcome. Shown and stored as lower-case text. */
public enum DealStage {
    OPEN("open"),
    WON("won"),
    LOST("lost");

    static final String ALLOWED = "open, won, lost";

    private final String value;

    DealStage(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    /** The stage whose text is exactly {@code text}, or empty. */
    public static Optional<DealStage> parse(String text) {
        return Arrays.stream(values()).filter(stage -> stage.value.equals(text)).findFirst();
    }
}
