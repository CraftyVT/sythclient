package dev.cosmetix.cosmetic;

import java.util.Locale;

public enum CosmeticType {
    CAPE, WINGS, HAT, AURA;

    public String key() {
        return name().toLowerCase(Locale.ROOT);
    }
}
