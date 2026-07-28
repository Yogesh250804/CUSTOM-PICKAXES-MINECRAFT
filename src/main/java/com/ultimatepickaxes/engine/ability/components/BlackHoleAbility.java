package com.ultimatepickaxes.engine.ability.components;

import com.google.gson.JsonObject;
import com.ultimatepickaxes.engine.ability.AbilityComponent;
import com.ultimatepickaxes.engine.ability.AbilityContext;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;

public class BlackHoleAbility implements AbilityComponent {

    @Override
    public boolean execute(JsonObject params, AbilityContext context) {
        if (!(context.getWorld() instanceof ServerWorld world) || context.getPlayer() == null) {
            return false;
        }

        Vec3d eyePos = context.getPlayer().getEyePos();
        Vec3d look = context.getPlayer().getRotationVector();
        double distance = params.has("distance") ? params.get("distance").getAsDouble() : 10.0;
        Vec3d center = eyePos.add(look.multiply(distance));

        world.playSound(null, context.getPlayer().getBlockPos(), SoundEvents.BLOCK_RESPAWN_ANCHOR_DEPLETE.value(), SoundCategory.PLAYERS, 2.0f, 0.5f);

        // Render black hole singularity with portal and squid ink particles
        world.spawnParticles(ParticleTypes.REVERSE_PORTAL, center.x, center.y, center.z, 150, 1.5, 1.5, 1.5, 0.2);
        world.spawnParticles(ParticleTypes.SQUID_INK, center.x, center.y, center.z, 60, 0.8, 0.8, 0.8, 0.05);
        world.spawnParticles(ParticleTypes.DRAGON_BREATH, center.x, center.y, center.z, 50, 1.0, 1.0, 1.0, 0.1);

        // Gravitational pull & damage to mobs
        Box box = new Box(center.add(-8, -8, -8), center.add(8, 8, 8));
        for (LivingEntity entity : world.getEntitiesByClass(LivingEntity.class, box, e -> e != context.getPlayer())) {
            entity.damage(world.getDamageSources().magic(), 18.0f);
            Vec3d pull = center.subtract(entity.getPos()).normalize().multiply(1.2);
            entity.setVelocity(pull);
            entity.velocityModified = true;

            if (entity instanceof ServerPlayerEntity serverPlayer) {
                serverPlayer.networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(serverPlayer));
            }
        }

        // Pull nearby dropped items to center
        for (ItemEntity item : world.getEntitiesByClass(ItemEntity.class, box, e -> true)) {
            Vec3d pull = center.subtract(item.getPos()).normalize().multiply(0.8);
            item.setVelocity(pull);
            item.velocityModified = true;
        }

        return true;
    }
}
