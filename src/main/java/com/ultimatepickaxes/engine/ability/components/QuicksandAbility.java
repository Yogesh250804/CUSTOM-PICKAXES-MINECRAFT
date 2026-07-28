package com.ultimatepickaxes.engine.ability.components;

import com.google.gson.JsonObject;
import com.ultimatepickaxes.engine.ability.AbilityComponent;
import com.ultimatepickaxes.engine.ability.AbilityContext;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

public class QuicksandAbility implements AbilityComponent {

    @Override
    public boolean execute(JsonObject params, AbilityContext context) {
        if (!(context.getWorld() instanceof ServerWorld world) || context.getPlayer() == null) {
            return false;
        }

        int radius = params.has("radius") ? params.get("radius").getAsInt() : 5;
        int duration = params.has("duration") ? params.get("duration").getAsInt() : 100;

        BlockPos center = context.getPlayer().getBlockPos().offset(context.getPlayer().getHorizontalFacing(), 3);

        world.playSound(null, center, SoundEvents.BLOCK_SAND_BREAK, SoundCategory.PLAYERS, 1.5f, 0.5f);

        // Create quicksand zone - replace top blocks with sand + soul sand to slow
        for (BlockPos pos : BlockPos.iterate(center.add(-radius, -1, -radius), center.add(radius, 0, radius))) {
            BlockPos immutable = pos.toImmutable();
            if (!world.getBlockState(immutable).isAir() && world.getBlockState(immutable.up()).isAir()) {
                world.setBlockState(immutable, Blocks.SOUL_SAND.getDefaultState());
            }
        }

        // Spawn sand particles
        BlockStateParticleEffect sandParticle = new BlockStateParticleEffect(ParticleTypes.BLOCK, Blocks.SAND.getDefaultState());
        world.spawnParticles(sandParticle, center.getX(), center.getY() + 1, center.getZ(), 50, radius / 2.0, 0.5, radius / 2.0, 0.1);

        // Apply extreme slowness + mining fatigue to mobs in the zone
        Box area = new Box(center).expand(radius);
        for (LivingEntity entity : world.getEntitiesByClass(LivingEntity.class, area, e -> e != context.getPlayer())) {
            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, duration, 6, false, true));
            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE, duration, 3, false, true));
            // Push mobs downward (sinking effect)
            entity.setVelocity(0, -0.5, 0);
            entity.velocityModified = true;
            entity.damage(world.getDamageSources().inWall(), 4.0f);
        }

        return true;
    }
}
