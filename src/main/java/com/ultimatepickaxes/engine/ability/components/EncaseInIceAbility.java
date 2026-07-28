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

public class EncaseInIceAbility implements AbilityComponent {

    @Override
    public boolean execute(JsonObject params, AbilityContext context) {
        if (!(context.getWorld() instanceof ServerWorld world) || context.getPlayer() == null) {
            return false;
        }

        int radius = params.has("radius") ? params.get("radius").getAsInt() : 8;
        int duration = params.has("duration") ? params.get("duration").getAsInt() : 160;

        BlockPos center = context.getPlayer().getBlockPos();
        world.playSound(null, center, SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.PLAYERS, 2.0f, 0.5f);

        Box area = new Box(center).expand(radius);
        for (LivingEntity entity : world.getEntitiesByClass(LivingEntity.class, area, e -> e != context.getPlayer())) {
            BlockPos pos = entity.getBlockPos();
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = 0; dy <= 2; dy++) {
                    for (int dz = -1; dz <= 1; dz++) {
                        BlockPos target = pos.add(dx, dy, dz);
                        if (world.getBlockState(target).isAir()) {
                            world.setBlockState(target, Blocks.BLUE_ICE.getDefaultState());
                        }
                    }
                }
            }

            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, duration, 127, false, false));
            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE, duration, 127, false, false));
            entity.damage(world.getDamageSources().freeze(), 8.0f);
            world.spawnParticles(ParticleTypes.SNOWFLAKE, entity.getX(), entity.getY() + 1, entity.getZ(), 40, 0.5, 1.0, 0.5, 0.1);
        }
        return true;
    }
}
