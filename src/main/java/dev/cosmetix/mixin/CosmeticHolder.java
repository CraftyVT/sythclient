package dev.cosmetix.mixin;

import java.util.UUID;

/** Duck-Interface: hängt die Spieler-UUID an den PlayerEntityRenderState. */
public interface CosmeticHolder {
    UUID cosmetix$getUuid();

    void cosmetix$setUuid(UUID uuid);
}
