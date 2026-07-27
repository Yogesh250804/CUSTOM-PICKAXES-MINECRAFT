package com.ultimatepickaxes.client;

import com.ultimatepickaxes.engine.effect.ScreenShakePayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;

import java.util.Random;

public class ScreenShakeClient {
    private static float shakeIntensity = 0.0f;
    private static int shakeTicks = 0;
    private static final Random random = new Random();

    public static void init() {
        ClientPlayNetworking.registerGlobalReceiver(ScreenShakePayload.ID, (payload, context) -> {
            context.client().execute(() -> {
                shakeIntensity = payload.intensity();
                shakeTicks = payload.durationTicks();
            });
        });
    }

    public static void tick() {
        if (shakeTicks > 0) {
            shakeTicks--;
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.player != null && client.world != null) {
                float rx = (random.nextFloat() - 0.5f) * shakeIntensity;
                float ry = (random.nextFloat() - 0.5f) * shakeIntensity;
                client.player.setPitch(client.player.getPitch() + rx);
                client.player.setYaw(client.player.getYaw() + ry);
            }
        }
    }
}
