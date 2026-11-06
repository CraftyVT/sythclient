package dev.cosmetix.cosmetic;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static dev.cosmetix.cosmetic.CosmeticType.*;

public final class CosmeticRegistry {
    private static final Map<String, Cosmetic> BY_ID = new LinkedHashMap<>();

    static {
        register("aurora", CAPE, false);
        register("ember", CAPE, false);
        register("angel", WINGS, false);
        register("phantom", WINGS, true);
        register("crown", HAT, false);
        register("halo", HAT, true);
        register("wizard", HAT, false);
        register("starlight", AURA, false);
        register("inferno", AURA, false);
        register("spirit", AURA, false);
    }

    private CosmeticRegistry() {}

    private static void register(String id, CosmeticType type, boolean glow) {
        BY_ID.put(id, new Cosmetic(id, type, glow));
    }

    public static Cosmetic get(String id) {
        return id == null ? null : BY_ID.get(id);
    }

    public static List<Cosmetic> byType(CosmeticType type) {
        List<Cosmetic> list = new ArrayList<>();
        for (Cosmetic c : BY_ID.values()) {
            if (c.type() == type) list.add(c);
        }
        return list;
    }
}
