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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

public class FreezeAbility implements AbilityComponent {

    @Override
    public boolean execute(JsonObject params, AbilityContext context) {
        if (!(context.getWorld() instanceof ServerWorld world) || context.getPlayer() == null) {
            return false;
        }

        int radius = params.has("radius") ? params.get("radius").getAsInt() : 6;
        int duration = params.has("duration") ? params.get("duration").getAsInt() : 120;
        BlockPos center = context.getPlayer().getBlockPos();

        for (BlockPos pos : BlockPos.iterate(center.add(-radius, -2, -radius), center.add(radius, 2, radius))) {
            if (world.getBlockState(pos).isOf(Blocks.WATER)) {
                world.setBlockState(pos, Blocks.FROSTED_ICE.getDefaultState());
            }
        }

        Box area = new Box(center).expand(radius);
        world.spawnParticles(ParticleTypes.SNOWFLAKE, center.getX(), center.getY() + 1, center.getZ(), 40, radius / 2.0, 1.0, radius / 2.0, 0.1);

        for (LivingEntity entity : world.getEntitiesByClass(LivingEntity.class, area, e -> e != context.getPlayer())) {
            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, duration, 4));
            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, duration, 2));
            entity.damage(world.getDamageSources().freeze(), 3.0f);
        }
        return true;
    }
}
