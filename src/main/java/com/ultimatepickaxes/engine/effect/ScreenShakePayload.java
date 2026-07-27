package com.ultimatepickaxes.engine.effect;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record ScreenShakePayload(float intensity, int durationTicks) implements CustomPayload {
    public static final CustomPayload.Id<ScreenShakePayload> ID = new CustomPayload.Id<>(Identifier.of("ultimatepickaxes", "screen_shake"));
    public static final PacketCodec<RegistryByteBuf, ScreenShakePayload> CODEC = PacketCodec.tuple(
        PacketCodecs.FLOAT, ScreenShakePayload::intensity,
        PacketCodecs.INTEGER, ScreenShakePayload::durationTicks,
        ScreenShakePayload::new
    );

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
}
