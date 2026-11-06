package dev.cosmetix.mixin;

import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.UUID;

@Mixin(PlayerEntityRenderState.class)
public class PlayerEntityRenderStateMixin implements CosmeticHolder {
    @Unique
    private UUID cosmetix$uuid;

    @Override
    public UUID cosmetix$getUuid() {
        return cosmetix$uuid;
    }

    @Override
    public void cosmetix$setUuid(UUID uuid) {
        this.cosmetix$uuid = uuid;
    }
}
