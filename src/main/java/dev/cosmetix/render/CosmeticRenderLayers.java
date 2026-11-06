package dev.cosmetix.render;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.util.Identifier;

/**
 * Einziger Ort, an dem RenderLayer-Fabriken angefasst werden.
 * Falls 1.21.11 den Namen anders mappt: nur diese Zeile anpassen.
 */
public final class CosmeticRenderLayers {
    private CosmeticRenderLayers() {}

    public static RenderLayer entity(Identifier texture) {
        return RenderLayers.getEntityCutoutNoCull(texture);
    }
}
