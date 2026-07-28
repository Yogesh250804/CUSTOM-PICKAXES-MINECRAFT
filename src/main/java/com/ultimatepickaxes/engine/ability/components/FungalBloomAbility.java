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

public class FungalBloomAbility implements AbilityComponent {

    @Override
    public boolean execute(JsonObject params, AbilityContext context) {
        if (!(context.getWorld() instanceof ServerWorld world) || context.getPlayer() == null) {
            return false;
        }

        int radius = params.has("radius") ? params.get("radius").getAsInt() : 8;
        BlockPos center = context.getPlayer().getBlockPos();

        world.playSound(null, center, SoundEvents.BLOCK_GRASS_STEP, SoundCategory.PLAYERS, 2.0f, 0.6f);

        for (BlockPos pos : BlockPos.iterate(center.add(-radius, -1, -radius), center.add(radius, 0, radius))) {
            BlockPos immutable = pos.toImmutable();
            if (!world.getBlockState(immutable).isAir() && world.getBlockState(immutable.up()).isAir()) {
                if (world.getRandom().nextFloat() < 0.5f) {
                    world.setBlockState(immutable, Blocks.MYCELIUM.getDefaultState());
                }
            }
        }

        world.spawnParticles(ParticleTypes.WARPED_SPORE, center.getX(), center.getY() + 1, center.getZ(), 80, radius / 2.0, 1.0, radius / 2.0, 0.1);

        Box area = new Box(center).expand(radius);
        for (LivingEntity entity : world.getEntitiesByClass(LivingEntity.class, area, e -> e != context.getPlayer())) {
            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 100, 1, false, true));
            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 100, 2, false, true));
        }
        return true;
    }
}
