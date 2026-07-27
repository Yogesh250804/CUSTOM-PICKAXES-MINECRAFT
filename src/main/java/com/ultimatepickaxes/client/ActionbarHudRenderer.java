package com.ultimatepickaxes.client;

import com.ultimatepickaxes.engine.cooldown.CooldownManager;
import com.ultimatepickaxes.items.UltimatePickaxeItem;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

public class ActionbarHudRenderer {

    public static void init() {
        HudRenderCallback.EVENT.register((drawContext, tickDelta) -> {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.player == null) return;

            ItemStack mainHand = client.player.getMainHandStack();
            if (mainHand.getItem() instanceof UltimatePickaxeItem pickaxe) {
                String id = pickaxe.getDefinition().getId();
                int maxCooldown = pickaxe.getDefinition().getCooldown();
                int remaining = CooldownManager.getRemainingTicks(client.player.getUuid(), id);

                String text;
                if (remaining > 0) {
                    float secs = remaining / 20.0f;
                    int progressBars = (int) ((1.0f - ((float) remaining / maxCooldown)) * 10);
                    StringBuilder sb = new StringBuilder("§c[");
                    for (int i = 0; i < 10; i++) {
                        sb.append(i < progressBars ? "■" : "□");
                    }
                    sb.append(String.format(" Cooldown: %.1fs]", secs));
                    text = sb.toString();
                } else {
                    text = "§a[⚡ Ability Ready (Right-Click) ⚡]";
                }

                renderText(drawContext, client.textRenderer, text);
            }
        });
    }

    private static void renderText(DrawContext drawContext, TextRenderer textRenderer, String text) {
        int width = drawContext.getScaledWindowWidth();
        int height = drawContext.getScaledWindowHeight();
        int x = (width - textRenderer.getWidth(text)) / 2;
        int y = height - 58;

        drawContext.drawTextWithShadow(textRenderer, Text.literal(text), x, y, 0xFFFFFF);
    }
}
