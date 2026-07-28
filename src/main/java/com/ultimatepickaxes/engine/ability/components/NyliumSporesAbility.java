package com.ultimatepickaxes.engine.ability.components;

import com.google.gson.JsonObject;
import com.ultimatepickaxes.engine.ability.AbilityComponent;
import com.ultimatepickaxes.engine.ability.AbilityContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

public class NyliumSporesAbility implements AbilityComponent {

    @Override
    public boolean execute(JsonObject params, AbilityContext context) {
        if (!(context.getWorld() instanceof ServerWorld world) || context.getPlayer() == null) {
            return false;
        }

        int radius = params.has("radius") ? params.get("radius").getAsInt() : 8;
        int duration = params.has("duration") ? params.get("duration").getAsInt() : 120;

        BlockPos center = context.getPlayer().getBlockPos();
        world.playSound(null, center, SoundEvents.BLOCK_NYLIUM_STEP, SoundCategory.PLAYERS, 2.0f, 0.8f);
        world.spawnParticles(ParticleTypes.CRIMSON_SPORE, center.getX() + 0.5, center.getY() + 1, center.getZ() + 0.5, 100, radius / 2.0, 1.0, radius / 2.0, 0.1);

        Box area = new Box(center).expand(radius);
        for (LivingEntity entity : world.getEntitiesByClass(LivingEntity.class, area, e -> e != context.getPlayer())) {
            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, duration, 1, false, true));
            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, duration, 0, false, true));
        }
        return true;
    }
}
