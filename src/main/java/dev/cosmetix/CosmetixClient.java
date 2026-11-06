package dev.cosmetix;

import dev.cosmetix.cosmetic.CosmeticManager;
import dev.cosmetix.gui.CosmeticScreen;
import dev.cosmetix.render.AuraEffects;
import dev.cosmetix.render.CosmeticFeatureRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

public class CosmetixClient implements ClientModInitializer {
    public static final String MOD_ID = "cosmetix";

    private static KeyBinding menuKey;

    public static Identifier id(String path) {
        return Identifier.of(MOD_ID, path);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onInitializeClient() {
        CosmeticManager.load();

        KeyBinding.Category category = KeyBinding.Category.create(id("main"));
        menuKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.cosmetix.menu", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_RIGHT_SHIFT, category));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (menuKey.wasPressed()) {
                client.setScreen(new CosmeticScreen(null));
            }
            AuraEffects.tick(client);
        });

        LivingEntityFeatureRendererRegistrationCallback.EVENT.register((entityType, renderer, helper, ctx) -> {
            if (renderer instanceof PlayerEntityRenderer<?>) {
                helper.register(new CosmeticFeatureRenderer(
                        (FeatureRendererContext<PlayerEntityRenderState, PlayerEntityModel>) renderer));
            }
        });
    }
}
