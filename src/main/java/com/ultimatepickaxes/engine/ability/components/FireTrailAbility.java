package com.ultimatepickaxes.engine.ability.components;

import com.google.gson.JsonObject;
import com.ultimatepickaxes.engine.ability.AbilityComponent;
import com.ultimatepickaxes.engine.ability.AbilityContext;
import net.minecraft.block.Blocks;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;

public class FireTrailAbility implements AbilityComponent {

    @Override
    public boolean execute(JsonObject params, AbilityContext context) {
        if (!(context.getWorld() instanceof ServerWorld world) || context.getPlayer() == null) {
            return false;
        }

        int radius = params.has("radius") ? params.get("radius").getAsInt() : 3;
        BlockPos center = context.getPlayer().getBlockPos();

        world.playSound(null, center, SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.PLAYERS, 1.5f, 0.9f);
        context.getPlayer().addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 300, 0, false, false));

        for (BlockPos pos : BlockPos.iterate(center.add(-radius, 0, -radius), center.add(radius, 0, radius))) {
            BlockPos immutable = pos.toImmutable();
            if (world.getBlockState(immutable).isAir() && !world.getBlockState(immutable.down()).isAir()) {
                world.setBlockState(immutable, Blocks.FIRE.getDefaultState());
            }
        }

        world.spawnParticles(ParticleTypes.FLAME, center.getX() + 0.5, center.getY() + 1, center.getZ() + 0.5, 40, radius / 2.0, 0.5, radius / 2.0, 0.1);
        return true;
    }
}
