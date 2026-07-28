package com.ultimatepickaxes.engine.ability.components;

import com.google.gson.JsonObject;
import com.ultimatepickaxes.engine.ability.AbilityComponent;
import com.ultimatepickaxes.engine.ability.AbilityContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;

public class TimeFreezeAbility implements AbilityComponent {

    @Override
    public boolean execute(JsonObject params, AbilityContext context) {
        if (!(context.getWorld() instanceof ServerWorld world) || context.getPlayer() == null) {
            return false;
        }

        Vec3d playerPos = context.getPlayer().getPos();
        world.playSound(null, context.getPlayer().getBlockPos(), SoundEvents.BLOCK_NOTE_BLOCK_CHIME.value(), SoundCategory.PLAYERS, 2.0f, 0.5f);
        world.playSound(null, context.getPlayer().getBlockPos(), SoundEvents.BLOCK_BEACON_DEACTIVATE, SoundCategory.PLAYERS, 1.5f, 1.5f);

        // Time dilation aura particles
        for (int i = 0; i < 120; i++) {
            double angle = (i / 120.0) * Math.PI * 2;
            double radius = 3.0 + (i % 5) * 1.5;
            double px = playerPos.x + Math.cos(angle) * radius;
            double pz = playerPos.z + Math.sin(angle) * radius;
            world.spawnParticles(ParticleTypes.END_ROD, px, playerPos.y + 1.0, pz, 1, 0.0, 0.05, 0.0, 0.02);
            world.spawnParticles(ParticleTypes.INSTANT_EFFECT, px, playerPos.y + 1.0, pz, 1, 0.0, 0.0, 0.0, 0.0);
        }

        // Freeze all entities in 15 block radius (Slowness 127 + Weakness 127)
        Box box = new Box(playerPos.add(-15, -6, -15), playerPos.add(15, 6, 15));
        for (LivingEntity entity : world.getEntitiesByClass(LivingEntity.class, box, e -> e != context.getPlayer())) {
            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 160, 127, true, true));
            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 160, 127, true, true));
            entity.setVelocity(Vec3d.ZERO);
            entity.velocityModified = true;

            if (entity instanceof ServerPlayerEntity serverPlayer) {
                serverPlayer.networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(serverPlayer));
            }
        }

        return true;
    }
}
