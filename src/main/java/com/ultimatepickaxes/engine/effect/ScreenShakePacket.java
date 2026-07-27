package com.ultimatepickaxes.engine.effect;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;

public class ScreenShakePacket {

    public static void send(ServerPlayerEntity player, float intensity, int durationTicks) {
        ServerPlayNetworking.send(player, new ScreenShakePayload(intensity, durationTicks));
    }
}
