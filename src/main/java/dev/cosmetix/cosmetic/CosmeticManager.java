package dev.cosmetix.cosmetic;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Hält das lokale Loadout und liefert es pro Spieler.
 * Heute: nur der lokale Spieler hat Cosmetics.
 * Später: get(UUID, type) per Backend/Server-Lookup erweitern, dann sehen auch andere deine Cosmetics.
 */
public final class CosmeticManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path FILE = FabricLoader.getInstance().getConfigDir().resolve("cosmetix.json");
    private static final EnumMap<CosmeticType, String> LOCAL = new EnumMap<>(CosmeticType.class);

    private CosmeticManager() {}

    public static void load() {
        if (!Files.exists(FILE)) return;
        try (Reader r = Files.newBufferedReader(FILE)) {
            Map<String, String> map = GSON.fromJson(r, new TypeToken<Map<String, String>>() {}.getType());
            if (map == null) return;
            for (CosmeticType type : CosmeticType.values()) {
                String id = map.get(type.key());
                if (CosmeticRegistry.get(id) != null) LOCAL.put(type, id);
            }
        } catch (Exception e) {
            System.err.println("[Cosmetix] Konnte Config nicht laden: " + e);
        }
    }

    public static void save() {
        Map<String, String> map = new HashMap<>();
        LOCAL.forEach((type, id) -> map.put(type.key(), id));
        try (Writer w = Files.newBufferedWriter(FILE)) {
            GSON.toJson(map, w);
        } catch (IOException e) {
            System.err.println("[Cosmetix] Konnte Config nicht speichern: " + e);
        }
    }

    public static Cosmetic getLocal(CosmeticType type) {
        return CosmeticRegistry.get(LOCAL.get(type));
    }

    /** Cosmetic eines beliebigen Spielers (UUID) oder null. */
    public static Cosmetic get(UUID player, CosmeticType type) {
        var self = MinecraftClient.getInstance().player;
        if (self != null && self.getUuid().equals(player)) {
            return getLocal(type);
        }
        return null; // TODO: Backend-Lookup für andere Spieler
    }

    /** Schaltet durch: Keins -> 1. -> 2. -> ... -> Keins */
    public static void cycle(CosmeticType type) {
        List<Cosmetic> all = CosmeticRegistry.byType(type);
        Cosmetic current = getLocal(type);
        int next = current == null ? 0 : all.indexOf(current) + 1;
        if (next >= all.size()) {
            LOCAL.remove(type);
        } else {
            LOCAL.put(type, all.get(next).id());
        }
        save();
    }
}
