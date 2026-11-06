package dev.cosmetix.render;

import dev.cosmetix.cosmetic.Cosmetic;
import dev.cosmetix.cosmetic.CosmeticManager;
import dev.cosmetix.cosmetic.CosmeticType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.MathHelper;

/** Partikel-Auren, einmal pro Client-Tick für jeden Spieler mit Aura. */
public final class AuraEffects {
    private AuraEffects() {}

    public static void tick(MinecraftClient mc) {
        ClientWorld world = mc.world;
        if (world == null || mc.isPaused()) return;
        long time = world.getTime();

        for (AbstractClientPlayerEntity p : world.getPlayers()) {
            Cosmetic aura = CosmeticManager.get(p.getUuid(), CosmeticType.AURA);
            if (aura == null || p.isInvisible()) continue;
            switch (aura.id()) {
                case "starlight" -> starlight(world, p, time);
                case "inferno" -> inferno(world, p, time);
                case "spirit" -> spirit(world, p, time);
                default -> { }
            }
        }
    }

    /** Doppelhelix aus End-Rod-Funken, die am Körper hochwandert. */
    private static void starlight(ClientWorld w, AbstractClientPlayerEntity p, long time) {
        double height = ((time % 40) / 40.0) * p.getHeight();
        for (int k = 0; k < 2; k++) {
            double a = time * 0.35 + k * Math.PI;
            w.addParticleClient(ParticleTypes.END_ROD,
                    p.getX() + Math.cos(a) * 0.65, p.getY() + height, p.getZ() + Math.sin(a) * 0.65,
                    0.0, 0.01, 0.0);
        }
    }

    /** Flammen steigen aus einem Ring um die Füße. */
    private static void inferno(ClientWorld w, AbstractClientPlayerEntity p, long time) {
        if (time % 2 != 0) return;
        double a = w.getRandom().nextDouble() * Math.PI * 2;
        double r = 0.35 + w.getRandom().nextDouble() * 0.3;
        w.addParticleClient(ParticleTypes.FLAME,
                p.getX() + Math.cos(a) * r, p.getY() + 0.1, p.getZ() + Math.sin(a) * r,
                0.0, 0.04, 0.0);
    }

    /** Blaue Seelenflammen kreisen auf Hüfthöhe und schweben auf und ab. */
    private static void spirit(ClientWorld w, AbstractClientPlayerEntity p, long time) {
        for (int k = 0; k < 3; k++) {
            double a = time * 0.12 + k * (Math.PI * 2 / 3);
            double y = p.getHeight() * 0.5 + MathHelper.sin(time * 0.1f + k) * 0.25;
            w.addParticleClient(ParticleTypes.SOUL_FIRE_FLAME,
                    p.getX() + Math.cos(a) * 0.8, p.getY() + y, p.getZ() + Math.sin(a) * 0.8,
                    0.0, 0.0, 0.0);
        }
    }
}
