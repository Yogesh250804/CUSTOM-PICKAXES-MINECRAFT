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

public class SugarRushAbility implements AbilityComponent {

    @Override
    public boolean execute(JsonObject params, AbilityContext context) {
        if (context.getPlayer() == null) return false;

        int duration = params.has("duration") ? params.get("duration").getAsInt() : 160;

        context.getPlayer().addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, duration, 4, false, true));
        context.getPlayer().addStatusEffect(new StatusEffectInstance(StatusEffects.HASTE, duration, 3, false, true));
        context.getPlayer().addStatusEffect(new StatusEffectInstance(StatusEffects.JUMP_BOOST, duration, 2, false, true));
        context.getPlayer().getHungerManager().add(10, 0.8f);

        if (context.getWorld() instanceof ServerWorld serverWorld) {
            serverWorld.playSound(null, context.getPlayer().getBlockPos(), SoundEvents.ENTITY_GENERIC_EAT, SoundCategory.PLAYERS, 1.5f, 1.2f);
            serverWorld.spawnParticles(ParticleTypes.HEART, context.getPlayer().getX(), context.getPlayer().getY() + 1, context.getPlayer().getZ(), 20, 0.5, 0.5, 0.5, 0.1);
        }
        return true;
    }
}
