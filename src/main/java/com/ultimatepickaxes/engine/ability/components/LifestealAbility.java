package com.ultimatepickaxes.engine.ability.components;

import com.google.gson.JsonObject;
import com.ultimatepickaxes.engine.ability.AbilityComponent;
import com.ultimatepickaxes.engine.ability.AbilityContext;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;

public class LifestealAbility implements AbilityComponent {

    @Override
    public boolean execute(JsonObject params, AbilityContext context) {
        if (context.getPlayer() == null) return false;

        int duration = params.has("duration") ? params.get("duration").getAsInt() : 200;
        int amplifier = params.has("amplifier") ? params.get("amplifier").getAsInt() : 1;

        context.getPlayer().addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, duration, amplifier, false, true));
        context.getPlayer().addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, duration, amplifier, false, true));
        context.getPlayer().addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, duration, 0, false, true));

        if (context.getWorld() instanceof ServerWorld serverWorld) {
            serverWorld.playSound(null, context.getPlayer().getBlockPos(), SoundEvents.ENTITY_ZOMBIFIED_PIGLIN_ANGRY, SoundCategory.PLAYERS, 1.5f, 0.8f);
            serverWorld.spawnParticles(ParticleTypes.ANGRY_VILLAGER, context.getPlayer().getX(), context.getPlayer().getY() + 1, context.getPlayer().getZ(), 20, 0.5, 0.5, 0.5, 0.1);
        }
        return true;
    }
}
