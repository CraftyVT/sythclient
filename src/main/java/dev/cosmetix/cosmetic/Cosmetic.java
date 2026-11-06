package dev.cosmetix.cosmetic;

import dev.cosmetix.CosmetixClient;
import net.minecraft.util.Identifier;

/** glow = wird immer voll beleuchtet gerendert (leuchtet auch in dunklen Höhlen). */
public record Cosmetic(String id, CosmeticType type, boolean glow) {

    public Identifier texture() {
        return CosmetixClient.id("textures/cosmetic/" + type.key() + "/" + id + ".png");
    }

    public String translationKey() {
        return "cosmetix.cosmetic." + id;
    }
}
