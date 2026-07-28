package com.ultimatepickaxes.engine.ability.components;

import com.google.gson.JsonObject;
import com.ultimatepickaxes.engine.ability.AbilityComponent;
import com.ultimatepickaxes.engine.ability.AbilityContext;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;

public class VolcanicEruptionAbility implements AbilityComponent {

    @Override
    public boolean execute(JsonObject params, AbilityContext context) {
        if (!(context.getWorld() instanceof ServerWorld world) || context.getPlayer() == null) {
            return false;
        }

        Vec3d eyePos = context.getPlayer().getEyePos();
        Vec3d look = context.getPlayer().getRotationVector();
        double distance = params.has("distance") ? params.get("distance").getAsDouble() : 8.0;
        BlockPos centerPos = BlockPos.ofFloored(eyePos.add(look.multiply(distance)));

        world.playSound(null, centerPos, SoundEvents.ENTITY_GENERIC_EXPLODE.value(), SoundCategory.PLAYERS, 2.0f, 0.7f);
        world.playSound(null, centerPos, SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.PLAYERS, 2.0f, 0.5f);

        // Volcanic eruption pillar particles
        for (int h = 0; h < 8; h++) {
            double py = centerPos.getY() + h;
            world.spawnParticles(ParticleTypes.LAVA, centerPos.getX() + 0.5, py, centerPos.getZ() + 0.5, 30, 0.6, 0.6, 0.6, 0.2);
            world.spawnParticles(ParticleTypes.FLAME, centerPos.getX() + 0.5, py, centerPos.getZ() + 0.5, 40, 0.8, 0.8, 0.8, 0.15);
            world.spawnParticles(ParticleTypes.LARGE_SMOKE, centerPos.getX() + 0.5, py + 2, centerPos.getZ() + 0.5, 20, 1.0, 1.0, 1.0, 0.1);
        }

        // Place magma blocks at ground surface
        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                BlockPos target = centerPos.add(dx, 0, dz);
                if (world.getBlockState(target).isAir() || world.getBlockState(target).getHardness(world, target) < 10) {
                    world.setBlockState(target, Blocks.MAGMA_BLOCK.getDefaultState());
                }
            }
        }

        // Damage mobs & ignite them
        Box box = new Box(centerPos.add(-4, -1, -4).toCenterPos(), centerPos.add(4, 9, 4).toCenterPos());
        for (LivingEntity entity : world.getEntitiesByClass(LivingEntity.class, box, e -> e != context.getPlayer())) {
            entity.damage(world.getDamageSources().onFire(), 14.0f);
            entity.setOnFireFor(8);
            entity.setVelocity(new Vec3d(0, 1.5, 0));
            entity.velocityModified = true;

            if (entity instanceof ServerPlayerEntity serverPlayer) {
                serverPlayer.networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(serverPlayer));
            }
        }

        return true;
    }
}
