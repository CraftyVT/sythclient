package dev.cosmetix.render;

import dev.cosmetix.cosmetic.Cosmetic;
import dev.cosmetix.cosmetic.CosmeticManager;
import dev.cosmetix.cosmetic.CosmeticType;
import dev.cosmetix.mixin.CosmeticHolder;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

import java.util.UUID;

public class CosmeticFeatureRenderer extends FeatureRenderer<PlayerEntityRenderState, PlayerEntityModel> {
    private static final int FULL_BRIGHT = 0xF000F0;

    public CosmeticFeatureRenderer(FeatureRendererContext<PlayerEntityRenderState, PlayerEntityModel> context) {
        super(context);
    }

    @Override
    public void render(MatrixStack matrices, OrderedRenderCommandQueue queue, int light,
                       PlayerEntityRenderState state, float limbAngle, float limbDistance) {
        if (state.invisible) return;
        UUID uuid = ((CosmeticHolder) state).cosmetix$getUuid();
        if (uuid == null) return;

        float t = state.age;

        Cosmetic cape = CosmeticManager.get(uuid, CosmeticType.CAPE);
        if (cape != null) renderCape(matrices, queue, light, cape, limbDistance);

        Cosmetic wings = CosmeticManager.get(uuid, CosmeticType.WINGS);
        if (wings != null) renderWings(matrices, queue, light, wings, t);

        Cosmetic hat = CosmeticManager.get(uuid, CosmeticType.HAT);
        if (hat != null) renderHat(matrices, queue, light, hat, t);
    }

    private void submit(ModelPart part, MatrixStack matrices, OrderedRenderCommandQueue queue,
                        int light, Cosmetic c) {
        queue.submitModelPart(part, matrices, CosmeticRenderLayers.entity(c.texture()),
                c.glow() ? FULL_BRIGHT : light, OverlayTexture.DEFAULT_UV, null);
    }

    private void renderCape(MatrixStack matrices, OrderedRenderCommandQueue queue, int light,
                            Cosmetic c, float limbDistance) {
        matrices.push();
        matrices.translate(0f, 0f, 0.125f);
        // Ruhewinkel 6 Grad + Schwung beim Laufen (wie Vanilla-Cape)
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(6f + limbDistance * 45f));
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180f));
        submit(CosmeticModels.CAPE, matrices, queue, light, c);
        matrices.pop();
    }

    private void renderWings(MatrixStack matrices, OrderedRenderCommandQueue queue, int light,
                             Cosmetic c, float t) {
        // sanftes Schlagen: Grundwinkel 0.35 rad +- 0.12 rad
        float flap = 0.35f + MathHelper.sin(t * 0.12f) * 0.12f;
        for (int side = -1; side <= 1; side += 2) {
            matrices.push();
            matrices.translate(side * 1.5f / 16f, 2f / 16f, 2f / 16f);
            matrices.multiply(RotationAxis.POSITIVE_Y.rotation(side * flap));
            submit(side > 0 ? CosmeticModels.WING_RIGHT : CosmeticModels.WING_LEFT, matrices, queue, light, c);
            matrices.pop();
        }
    }

    private void renderHat(MatrixStack matrices, OrderedRenderCommandQueue queue, int light,
                           Cosmetic c, float t) {
        matrices.push();
        getContextModel().head.applyTransform(matrices);
        switch (c.id()) {
            case "crown" -> submit(CosmeticModels.CROWN, matrices, queue, light, c);
            case "wizard" -> submit(CosmeticModels.WIZARD_HAT, matrices, queue, light, c);
            case "halo" -> {
                matrices.translate(0f, MathHelper.sin(t * 0.08f) * 0.03f, 0f);
                matrices.multiply(RotationAxis.POSITIVE_Y.rotation(t * 0.04f));
                submit(CosmeticModels.HALO, matrices, queue, light, c);
            }
            default -> { }
        }
        matrices.pop();
    }
}
