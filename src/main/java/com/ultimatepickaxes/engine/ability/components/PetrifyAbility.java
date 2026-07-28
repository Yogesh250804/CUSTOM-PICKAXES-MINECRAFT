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

public class PetrifyAbility implements AbilityComponent {

    @Override
    public boolean execute(JsonObject params, AbilityContext context) {
        if (!(context.getWorld() instanceof ServerWorld world) || context.getPlayer() == null) {
            return false;
        }

        int radius = params.has("radius") ? params.get("radius").getAsInt() : 8;
        int duration = params.has("duration") ? params.get("duration").getAsInt() : 120; // 6 seconds
        float shatterDamage = params.has("shatterDamage") ? params.get("shatterDamage").getAsFloat() : 15.0f;

        BlockPos center = context.getPlayer().getBlockPos();
        world.playSound(null, center, SoundEvents.BLOCK_STONE_PLACE, SoundCategory.PLAYERS, 2.0f, 0.3f);

        Box area = new Box(center).expand(radius);
        for (LivingEntity entity : world.getEntitiesByClass(LivingEntity.class, area, e -> e != context.getPlayer())) {
            // Encase mob in stone blocks (visual petrification)
            BlockPos mobPos = entity.getBlockPos();
            for (int dx = -1; dx <= 1; dx++) {
                for (int dz = -1; dz <= 1; dz++) {
                    for (int dy = 0; dy <= 2; dy++) {
                        BlockPos stonePos = mobPos.add(dx, dy, dz);
                        if (world.getBlockState(stonePos).isAir()) {
                            // Only place on the outer shell
                            if (Math.abs(dx) == 1 || Math.abs(dz) == 1 || dy == 0 || dy == 2) {
                                world.setBlockState(stonePos, Blocks.ANDESITE.getDefaultState());
                            }
                        }
                    }
                }
            }

            // Apply petrify effects - complete immobility
            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, duration, 127, false, false));
            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE, duration, 127, false, false));
            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, duration, 127, false, false));
            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, duration, 4, false, false));
            entity.setVelocity(0, 0, 0);
            entity.velocityModified = true;

            // After the freeze, they'll take shatter damage (applied as delayed damage)
            entity.damage(world.getDamageSources().magic(), shatterDamage);

            // Stone particles
            world.spawnParticles(ParticleTypes.CRIT, entity.getX(), entity.getY() + 1, entity.getZ(), 30, 0.5, 1.0, 0.5, 0.1);
        }

        return true;
    }
}
