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

public class AllBuffsAbility implements AbilityComponent {

    @Override
    public boolean execute(JsonObject params, AbilityContext context) {
        if (context.getPlayer() == null) return false;

        int duration = params.has("duration") ? params.get("duration").getAsInt() : 300;

        context.getPlayer().addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, duration, 1));
        context.getPlayer().addStatusEffect(new StatusEffectInstance(StatusEffects.HASTE, duration, 2));
        context.getPlayer().addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, duration, 1));
        context.getPlayer().addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, duration, 1));
        context.getPlayer().addStatusEffect(new StatusEffectInstance(StatusEffects.JUMP_BOOST, duration, 1));
        context.getPlayer().addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, duration, 1));
        context.getPlayer().addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, duration, 0));
        context.getPlayer().addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, duration, 0));

        if (context.getWorld() instanceof ServerWorld serverWorld) {
            serverWorld.playSound(null, context.getPlayer().getBlockPos(), SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.PLAYERS, 2.0f, 1.5f);
            serverWorld.spawnParticles(ParticleTypes.ENCHANT, context.getPlayer().getX(), context.getPlayer().getY() + 1, context.getPlayer().getZ(), 80, 0.5, 1.0, 0.5, 0.2);
        }
        return true;
    }
}
