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

public class DarkPulseAbility implements AbilityComponent {

    @Override
    public boolean execute(JsonObject params, AbilityContext context) {
        if (!(context.getWorld() instanceof ServerWorld world) || context.getPlayer() == null) {
            return false;
        }

        int radius = params.has("radius") ? params.get("radius").getAsInt() : 12;
        int duration = params.has("duration") ? params.get("duration").getAsInt() : 120;

        BlockPos center = context.getPlayer().getBlockPos();
        world.playSound(null, center, SoundEvents.ENTITY_WARDEN_HEARTBEAT, SoundCategory.PLAYERS, 2.0f, 0.5f);
        world.spawnParticles(ParticleTypes.SQUID_INK, center.getX() + 0.5, center.getY() + 1, center.getZ() + 0.5, 100, radius / 2.0, 1.0, radius / 2.0, 0.1);

        Box area = new Box(center).expand(radius);
        for (LivingEntity entity : world.getEntitiesByClass(LivingEntity.class, area, e -> e != context.getPlayer())) {
            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.DARKNESS, duration, 0, false, true));
            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, duration, 1, false, true));
            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, duration, 2, false, true));
        }
        return true;
    }
}
