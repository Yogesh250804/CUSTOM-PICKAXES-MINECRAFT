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

public class GoldenFeastAbility implements AbilityComponent {

    @Override
    public boolean execute(JsonObject params, AbilityContext context) {
        if (context.getPlayer() == null) return false;

        int duration = params.has("duration") ? params.get("duration").getAsInt() : 300;

        context.getPlayer().addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 100, 1, false, true));
        context.getPlayer().addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, duration, 3, false, true));
        context.getPlayer().addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, duration, 0, false, true));
        context.getPlayer().addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, duration, 0, false, true));
        context.getPlayer().getHungerManager().add(20, 1.0f);

        if (context.getWorld() instanceof ServerWorld serverWorld) {
            serverWorld.playSound(null, context.getPlayer().getBlockPos(), SoundEvents.ENTITY_PLAYER_BURP, SoundCategory.PLAYERS, 1.5f, 1.0f);
            serverWorld.spawnParticles(ParticleTypes.TOTEM_OF_UNDYING, context.getPlayer().getX(), context.getPlayer().getY() + 1, context.getPlayer().getZ(), 30, 0.5, 0.5, 0.5, 0.1);
        }
        return true;
    }
}
