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

public class IceAgeAbility implements AbilityComponent {

    @Override
    public boolean execute(JsonObject params, AbilityContext context) {
        if (!(context.getWorld() instanceof ServerWorld world) || context.getPlayer() == null) {
            return false;
        }

        BlockPos centerPos = context.getPlayer().getBlockPos();
        world.playSound(null, centerPos, SoundEvents.ENTITY_PLAYER_HURT_FREEZE, SoundCategory.PLAYERS, 2.5f, 0.5f);
        world.playSound(null, centerPos, SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.PLAYERS, 2.0f, 0.7f);

        int radius = 12;

        // Spawn blizzard & frost particles
        for (int r = 1; r <= radius; r += 2) {
            for (int i = 0; i < 25; i++) {
                double angle = (i / 25.0) * Math.PI * 2;
                double px = centerPos.getX() + 0.5 + Math.cos(angle) * r;
                double pz = centerPos.getZ() + 0.5 + Math.sin(angle) * r;
                double py = centerPos.getY() + 0.5;

                world.spawnParticles(ParticleTypes.SNOWFLAKE, px, py + 1.0, pz, 10, 0.4, 0.4, 0.4, 0.1);
                world.spawnParticles(ParticleTypes.CLOUD, px, py + 0.5, pz, 5, 0.2, 0.2, 0.2, 0.05);
            }
        }

        // Freeze water/ground into blue ice
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                if (dx * dx + dz * dz <= radius * radius) {
                    BlockPos target = centerPos.add(dx, -1, dz);
                    if (world.getBlockState(target).isOf(Blocks.WATER)) {
                        world.setBlockState(target, Blocks.BLUE_ICE.getDefaultState());
                    } else if (world.getBlockState(target.up()).isAir() && !world.getBlockState(target).isAir()) {
                        world.setBlockState(target.up(), Blocks.SNOW.getDefaultState());
                    }
                }
            }
        }

        // Encase mobs in ice cages & freeze
        Box box = new Box(centerPos.add(-radius, -3, -radius).toCenterPos(), centerPos.add(radius, 5, radius).toCenterPos());
        for (LivingEntity entity : world.getEntitiesByClass(LivingEntity.class, box, e -> e != context.getPlayer())) {
            entity.damage(world.getDamageSources().freeze(), 16.0f);
            entity.setFrozenTicks(300);
            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 200, 127, true, true));

            BlockPos ePos = entity.getBlockPos();
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = 0; dy <= 2; dy++) {
                    for (int dz = -1; dz <= 1; dz++) {
                        BlockPos cage = ePos.add(dx, dy, dz);
                        if (world.getBlockState(cage).isAir()) {
                            world.setBlockState(cage, Blocks.PACKED_ICE.getDefaultState());
                        }
                    }
                }
            }
        }

        return true;
    }
}
