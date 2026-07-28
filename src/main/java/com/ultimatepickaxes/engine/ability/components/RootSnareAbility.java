package com.ultimatepickaxes.engine.ability.components;

import com.google.gson.JsonObject;
import com.ultimatepickaxes.engine.ability.AbilityComponent;
import com.ultimatepickaxes.engine.ability.AbilityContext;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

public class RootSnareAbility implements AbilityComponent {

    @Override
    public boolean execute(JsonObject params, AbilityContext context) {
        if (!(context.getWorld() instanceof ServerWorld world) || context.getPlayer() == null) {
            return false;
        }

        int radius = params.has("radius") ? params.get("radius").getAsInt() : 6;
        int duration = params.has("duration") ? params.get("duration").getAsInt() : 100;

        BlockPos center = context.getPlayer().getBlockPos();
        world.playSound(null, center, SoundEvents.BLOCK_VINE_STEP, SoundCategory.PLAYERS, 1.5f, 0.7f);

        // Find and trap all mobs in range
        Box area = new Box(center).expand(radius);
        for (LivingEntity entity : world.getEntitiesByClass(LivingEntity.class, area, e -> e != context.getPlayer())) {
            BlockPos mobPos = entity.getBlockPos();

            // Place vine blocks around the mob to trap them
            for (int dx = -1; dx <= 1; dx++) {
                for (int dz = -1; dz <= 1; dz++) {
                    BlockPos vinePos = mobPos.add(dx, 0, dz);
                    if (world.getBlockState(vinePos).isAir()) {
                        world.setBlockState(vinePos, Blocks.MOSS_BLOCK.getDefaultState());
                    }
                    BlockPos vineAbove = vinePos.up();
                    if (world.getBlockState(vineAbove).isAir()) {
                        world.setBlockState(vineAbove, Blocks.MOSS_CARPET.getDefaultState());
                    }
                }
            }

            // Apply root effect - extreme slowness + no jump
            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, duration, 127, false, true));
            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.JUMP_BOOST, duration, 128, false, true));
            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, duration, 1, false, true));

            // Spawn vine particles
            world.spawnParticles(ParticleTypes.HAPPY_VILLAGER,
                    entity.getX(), entity.getY() + 1, entity.getZ(),
                    20, 0.5, 0.5, 0.5, 0.1);
        }

        // Spread moss around center
        for (BlockPos pos : BlockPos.iterate(center.add(-radius, -1, -radius), center.add(radius, 0, radius))) {
            if (world.getBlockState(pos.toImmutable()).isAir() && !world.getBlockState(pos.toImmutable().down()).isAir()) {
                if (world.getRandom().nextFloat() < 0.3f) {
                    world.setBlockState(pos.toImmutable(), Blocks.MOSS_CARPET.getDefaultState());
                }
            }
        }

        return true;
    }
}
