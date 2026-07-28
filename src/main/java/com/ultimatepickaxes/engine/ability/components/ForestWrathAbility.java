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

public class ForestWrathAbility implements AbilityComponent {

    @Override
    public boolean execute(JsonObject params, AbilityContext context) {
        if (!(context.getWorld() instanceof ServerWorld world) || context.getPlayer() == null) {
            return false;
        }

        int radius = params.has("radius") ? params.get("radius").getAsInt() : 6;
        BlockPos center = context.getPlayer().getBlockPos();

        world.playSound(null, center, SoundEvents.BLOCK_GRASS_BREAK, SoundCategory.PLAYERS, 2.0f, 0.8f);

        for (int i = 0; i < 4; i++) {
            int rx = (world.getRandom().nextInt(radius * 2) - radius);
            int rz = (world.getRandom().nextInt(radius * 2) - radius);
            BlockPos pos = center.add(rx, 0, rz);
            if (world.getBlockState(pos).isAir() && !world.getBlockState(pos.down()).isAir()) {
                world.setBlockState(pos, world.getRandom().nextBoolean() ? Blocks.BROWN_MUSHROOM.getDefaultState() : Blocks.RED_MUSHROOM.getDefaultState());
            }
        }

        world.spawnParticles(ParticleTypes.SPORE_BLOSSOM_AIR, center.getX(), center.getY() + 1, center.getZ(), 50, radius / 2.0, 1.0, radius / 2.0, 0.1);

        Box area = new Box(center).expand(radius);
        for (LivingEntity entity : world.getEntitiesByClass(LivingEntity.class, area, e -> e != context.getPlayer())) {
            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 120, 1, false, true));
            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 120, 0, false, true));
        }
        return true;
    }
}
