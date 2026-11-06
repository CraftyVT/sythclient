package dev.cosmetix.render;

import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;

/**
 * Alle Cosmetic-Geometrien, prozedural gebaut (keine Blockbench-Dateien nötig).
 * Die Parts sind STATISCH - Animationen laufen ausschließlich über den MatrixStack,
 * weil das Rendering in 1.21.9+ verzögert (Command-Queue) passiert.
 * Einheiten: Pixel (1/16 Block). Y zeigt nach unten, Z nach hinten.
 */
public final class CosmeticModels {

    public static final ModelPart CAPE = buildCape();
    public static final ModelPart WING_RIGHT = buildWing(1);
    public static final ModelPart WING_LEFT = buildWing(-1);
    public static final ModelPart CROWN = buildCrown();
    public static final ModelPart HALO = buildHalo();
    public static final ModelPart WIZARD_HAT = buildWizardHat();

    private CosmeticModels() {}

    private static ModelPart bake(ModelData data, int w, int h) {
        return TexturedModelData.of(data, w, h).createModel();
    }

    private static ModelPart buildCape() {
        ModelData data = new ModelData();
        data.getRoot().addChild("cape",
                ModelPartBuilder.create().uv(0, 0).cuboid(-5f, 0f, -1f, 10f, 16f, 1f),
                ModelTransform.NONE);
        return bake(data, 64, 32);
    }

    /** Fächer aus 6 Federn, nach außen kürzer werdend. side: 1 = rechts, -1 = links. */
    private static ModelPart buildWing(int side) {
        ModelData data = new ModelData();
        ModelPartData root = data.getRoot();
        float[] len = {18f, 17f, 15f, 13f, 11f, 9f};
        for (int i = 0; i < len.length; i++) {
            float l = len[i];
            float roll = -0.75f + i * 0.27f;
            ModelPartBuilder b = ModelPartBuilder.create().uv(0, i * 5)
                    .cuboid(side > 0 ? 0f : -l, -1.5f, 0f, l, 3f, 1f);
            // kleiner Z-Versatz pro Feder gegen Z-Fighting
            root.addChild("feather" + i, b, ModelTransform.of(0f, 0f, i * 0.12f, 0f, 0f, side * roll));
        }
        return bake(data, 64, 32);
    }

    private static ModelPart buildCrown() {
        ModelData data = new ModelData();
        ModelPartBuilder b = ModelPartBuilder.create().uv(0, 0)
                // Reif
                .cuboid(-4.5f, -10f, -4.5f, 9f, 2f, 1f)
                .cuboid(-4.5f, -10f, 3.5f, 9f, 2f, 1f)
                .cuboid(-4.5f, -10f, -3.5f, 1f, 2f, 7f)
                .cuboid(3.5f, -10f, -3.5f, 1f, 2f, 7f)
                // Zacken: 4 Ecken + 4 Mitten
                .cuboid(-4.5f, -12.5f, -4.5f, 1f, 2.5f, 1f)
                .cuboid(3.5f, -12.5f, -4.5f, 1f, 2.5f, 1f)
                .cuboid(-4.5f, -12.5f, 3.5f, 1f, 2.5f, 1f)
                .cuboid(3.5f, -12.5f, 3.5f, 1f, 2.5f, 1f)
                .cuboid(-0.5f, -13f, -4.5f, 1f, 3f, 1f)
                .cuboid(-0.5f, -13f, 3.5f, 1f, 3f, 1f)
                .cuboid(-4.5f, -13f, -0.5f, 1f, 3f, 1f)
                .cuboid(3.5f, -13f, -0.5f, 1f, 3f, 1f);
        data.getRoot().addChild("crown", b, ModelTransform.NONE);
        return bake(data, 64, 64);
    }

    /** Achteck-Ring aus 8 Segmenten, Mittelpunkt über dem Kopf. */
    private static ModelPart buildHalo() {
        ModelData data = new ModelData();
        ModelPartData root = data.getRoot();
        float r = 5.4f;
        for (int i = 0; i < 8; i++) {
            float a = (float) (i * Math.PI / 4.0);
            ModelPartBuilder b = ModelPartBuilder.create().uv(0, 0)
                    .cuboid(-2.3f, -0.75f, -0.75f, 4.6f, 1.5f, 1.5f);
            root.addChild("seg" + i, b,
                    ModelTransform.of((float) Math.sin(a) * r, -13f, (float) Math.cos(a) * r, 0f, a, 0f));
        }
        return bake(data, 64, 64);
    }

    /** Krempe + vier leicht abknickende Stufen. */
    private static ModelPart buildWizardHat() {
        ModelData data = new ModelData();
        ModelPartData root = data.getRoot();
        root.addChild("brim",
                ModelPartBuilder.create().uv(0, 0).cuboid(-7f, -1f, -7f, 14f, 1f, 14f),
                ModelTransform.origin(0f, -8.5f, 0f));
        ModelPartData t1 = root.addChild("t1",
                ModelPartBuilder.create().uv(0, 16).cuboid(-4.5f, -4f, -4.5f, 9f, 4f, 9f),
                ModelTransform.origin(0f, -9.5f, 0f));
        ModelPartData t2 = t1.addChild("t2",
                ModelPartBuilder.create().uv(0, 16).cuboid(-3f, -4f, -3f, 6f, 4f, 6f),
                ModelTransform.of(0f, -4f, 0f, -0.10f, 0f, 0f));
        ModelPartData t3 = t2.addChild("t3",
                ModelPartBuilder.create().uv(0, 16).cuboid(-1.8f, -4f, -1.8f, 3.6f, 4f, 3.6f),
                ModelTransform.of(0f, -4f, 0f, -0.22f, 0f, 0f));
        t3.addChild("t4",
                ModelPartBuilder.create().uv(0, 16).cuboid(-0.8f, -3.5f, -0.8f, 1.6f, 3.5f, 1.6f),
                ModelTransform.of(0f, -4f, 0f, -0.40f, 0f, 0f));
        return bake(data, 64, 64);
    }
}
