package dev.cosmetix.gui;

import dev.cosmetix.cosmetic.Cosmetic;
import dev.cosmetix.cosmetic.CosmeticManager;
import dev.cosmetix.cosmetic.CosmeticType;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;

/** Einfaches Menü: ein Button pro Slot, Klick schaltet durch die verfügbaren Cosmetics. */
public class CosmeticScreen extends Screen {
    private final Screen parent;

    public CosmeticScreen(Screen parent) {
        super(Text.translatable("cosmetix.screen.title"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int w = 200, h = 20;
        int x = this.width / 2 - w / 2;
        int y = this.height / 4 + 10;

        for (CosmeticType type : CosmeticType.values()) {
            ButtonWidget button = ButtonWidget.builder(label(type), btn -> {
                CosmeticManager.cycle(type);
                btn.setMessage(label(type));
            }).dimensions(x, y, w, h).build();
            addDrawableChild(button);
            y += 26;
        }

        addDrawableChild(ButtonWidget.builder(ScreenTexts.DONE, b -> close())
                .dimensions(x, y + 10, w, h).build());
    }

    private Text label(CosmeticType type) {
        Cosmetic c = CosmeticManager.getLocal(type);
        Text name = c == null ? Text.translatable("cosmetix.none") : Text.translatable(c.translationKey());
        return Text.translatable("cosmetix.type." + type.key()).append(": ").append(name);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, this.height / 4 - 20, 0xFFFFFFFF);
    }

    @Override
    public void close() {
        if (this.client != null) this.client.setScreen(parent);
    }
}
