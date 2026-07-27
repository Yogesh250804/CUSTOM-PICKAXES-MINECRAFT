package com.ultimatepickaxes.engine.effect;

import com.google.gson.JsonObject;
import com.ultimatepickaxes.engine.ability.AbilityContext;
import net.minecraft.network.packet.s2c.play.OverlayMessageS2CPacket;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class EffectPipeline {

    public static void playEffects(JsonObject effectsConfig, AbilityContext ctx) {
        if (effectsConfig == null || ctx == null) return;

        // Sound
        if (effectsConfig.has("sound")) {
            String soundStr = effectsConfig.get("sound").getAsString();
            SoundEvent soundEvent = Registries.SOUND_EVENT.get(Identifier.of(soundStr));
            if (soundEvent == null) soundEvent = SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP;
            if (ctx.getWorld() != null && ctx.getPos() != null) {
                ctx.getWorld().playSound(
                    null,
                    ctx.getPos(),
                    soundEvent,
                    SoundCategory.PLAYERS,
                    1.0f,
                    1.0f
                );
            }
        }

        // Particle
        if (effectsConfig.has("particle") && ctx.getWorld() instanceof ServerWorld serverWorld && ctx.getPos() != null) {
            String particleStr = effectsConfig.get("particle").getAsString();
            ParticleEffect particle = (ParticleEffect) Registries.PARTICLE_TYPE.get(Identifier.of(particleStr));
            if (particle == null) particle = ParticleTypes.POOF;
            int count = effectsConfig.has("particleCount") ? effectsConfig.get("particleCount").getAsInt() : 15;
            serverWorld.spawnParticles(
                particle,
                ctx.getPos().getX() + 0.5,
                ctx.getPos().getY() + 0.5,
                ctx.getPos().getZ() + 0.5,
                count,
                0.3, 0.3, 0.3,
                0.05
            );
        }

        // HUD Message
        if (effectsConfig.has("hudMessage") && ctx.getPlayer() instanceof ServerPlayerEntity serverPlayer) {
            String message = effectsConfig.get("hudMessage").getAsString();
            serverPlayer.networkHandler.sendPacket(new OverlayMessageS2CPacket(Text.literal(message)));
        }

        // Screen Shake
        if (effectsConfig.has("screenShake") && ctx.getPlayer() instanceof ServerPlayerEntity serverPlayer) {
            JsonObject shake = effectsConfig.getAsJsonObject("screenShake");
            float intensity = shake.has("intensity") ? shake.get("intensity").getAsFloat() : 0.5f;
            int duration = shake.has("duration") ? shake.get("duration").getAsInt() : 10;
            ScreenShakePacket.send(serverPlayer, intensity, duration);
        }
    }
}
