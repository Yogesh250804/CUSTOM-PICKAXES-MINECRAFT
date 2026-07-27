package com.ultimatepickaxes.engine.ability.components;

import com.google.gson.JsonObject;
import com.ultimatepickaxes.engine.ability.AbilityComponent;
import com.ultimatepickaxes.engine.ability.AbilityContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

public class BurnAbility implements AbilityComponent {

    @Override
    public boolean execute(JsonObject params, AbilityContext context) {
        if (!(context.getWorld() instanceof ServerWorld world) || context.getPlayer() == null) {
            return false;
        }

        int radius = params.has("radius") ? params.get("radius").getAsInt() : 6;
        int duration = params.has("duration") ? params.get("duration").getAsInt() : 6;
        BlockPos center = context.getPlayer().getBlockPos();

        Box area = new Box(center).expand(radius);
        world.spawnParticles(ParticleTypes.FLAME, center.getX(), center.getY() + 1, center.getZ(), 50, radius / 2.0, 1.0, radius / 2.0, 0.1);

        for (LivingEntity entity : world.getEntitiesByClass(LivingEntity.class, area, e -> e != context.getPlayer())) {
            entity.setOnFireFor(duration);
            entity.damage(world.getDamageSources().onFire(), 5.0f);
        }
        return true;
    }
}
