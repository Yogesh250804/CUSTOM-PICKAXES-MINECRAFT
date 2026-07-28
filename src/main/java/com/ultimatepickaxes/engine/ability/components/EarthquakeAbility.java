package com.ultimatepickaxes.engine.ability.components;

import com.google.gson.JsonObject;
import com.ultimatepickaxes.engine.ability.AbilityComponent;
import com.ultimatepickaxes.engine.ability.AbilityContext;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;

public class EarthquakeAbility implements AbilityComponent {

    @Override
    public boolean execute(JsonObject params, AbilityContext context) {
        if (!(context.getWorld() instanceof ServerWorld world) || context.getPlayer() == null) {
            return false;
        }

        BlockPos centerPos = context.getPlayer().getBlockPos();
        world.playSound(null, centerPos, SoundEvents.ENTITY_GENERIC_EXPLODE.value(), SoundCategory.PLAYERS, 2.5f, 0.4f);
        world.playSound(null, centerPos, SoundEvents.ENTITY_WARDEN_ROAR, SoundCategory.PLAYERS, 1.8f, 0.6f);

        BlockStateParticleEffect dirtParticle = new BlockStateParticleEffect(ParticleTypes.BLOCK, Blocks.DIRT.getDefaultState());
        BlockStateParticleEffect stoneParticle = new BlockStateParticleEffect(ParticleTypes.BLOCK, Blocks.STONE.getDefaultState());

        int radius = 15;
        for (int r = 1; r <= radius; r += 2) {
            for (int i = 0; i < 20; i++) {
                double angle = (i / 20.0) * Math.PI * 2;
                double px = centerPos.getX() + 0.5 + Math.cos(angle) * r;
                double pz = centerPos.getZ() + 0.5 + Math.sin(angle) * r;
                double py = centerPos.getY() + 0.5;

                world.spawnParticles(dirtParticle, px, py, pz, 15, 0.5, 0.8, 0.5, 0.2);
                world.spawnParticles(stoneParticle, px, py, pz, 10, 0.5, 0.8, 0.5, 0.2);
            }
        }

        // Crack ground blocks in radius
        for (int dx = -radius; dx <= radius; dx += 3) {
            for (int dz = -radius; dz <= radius; dz += 3) {
                BlockPos target = centerPos.add(dx, -1, dz);
                BlockState state = world.getBlockState(target);
                if (state.isOf(Blocks.GRASS_BLOCK) || state.isOf(Blocks.DIRT)) {
                    world.setBlockState(target, Blocks.COBBLESTONE.getDefaultState());
                } else if (state.isOf(Blocks.STONE)) {
                    world.setBlockState(target, Blocks.GRAVEL.getDefaultState());
                }
            }
        }

        // Knockdown and damage all mobs
        Box box = new Box(centerPos.add(-radius, -3, -radius).toCenterPos(), centerPos.add(radius, 5, radius).toCenterPos());
        for (LivingEntity entity : world.getEntitiesByClass(LivingEntity.class, box, e -> e != context.getPlayer())) {
            entity.damage(world.getDamageSources().playerAttack(context.getPlayer()), 22.0f);
            Vec3d launch = new Vec3d((Math.random() - 0.5) * 2.0, 1.2, (Math.random() - 0.5) * 2.0);
            entity.setVelocity(launch);
            entity.velocityModified = true;

            if (entity instanceof ServerPlayerEntity serverPlayer) {
                serverPlayer.networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(serverPlayer));
            }
        }

        return true;
    }
}
