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

public class BlizzardAbility implements AbilityComponent {

    @Override
    public boolean execute(JsonObject params, AbilityContext context) {
        if (!(context.getWorld() instanceof ServerWorld world) || context.getPlayer() == null) {
            return false;
        }

        int radius = params.has("radius") ? params.get("radius").getAsInt() : 10;
        int duration = params.has("duration") ? params.get("duration").getAsInt() : 100;

        BlockPos center = context.getPlayer().getBlockPos();
        world.playSound(null, center, SoundEvents.ENTITY_PLAYER_HURT_FREEZE, SoundCategory.PLAYERS, 2.0f, 0.7f);

        for (BlockPos pos : BlockPos.iterate(center.add(-radius, 0, -radius), center.add(radius, 1, radius))) {
            BlockPos immutable = pos.toImmutable();
            if (world.getBlockState(immutable).isAir() && !world.getBlockState(immutable.down()).isAir()) {
                if (world.getRandom().nextFloat() < 0.4f) {
                    world.setBlockState(immutable, Blocks.SNOW.getDefaultState());
                }
            }
        }

        world.spawnParticles(ParticleTypes.SNOWFLAKE, center.getX(), center.getY() + 2, center.getZ(), 100, radius / 2.0, 2.0, radius / 2.0, 0.2);

        Box area = new Box(center).expand(radius);
        for (LivingEntity entity : world.getEntitiesByClass(LivingEntity.class, area, e -> e != context.getPlayer())) {
            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, duration, 0, false, true));
            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, duration, 3, false, true));
            entity.damage(world.getDamageSources().freeze(), 5.0f);
        }
        return true;
    }
}
