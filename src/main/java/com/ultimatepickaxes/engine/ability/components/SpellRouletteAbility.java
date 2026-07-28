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

import java.util.Random;

public class SpellRouletteAbility implements AbilityComponent {
    private final Random random = new Random();

    @Override
    public boolean execute(JsonObject params, AbilityContext context) {
        if (!(context.getWorld() instanceof ServerWorld world) || context.getPlayer() == null) {
            return false;
        }

        world.playSound(null, context.getPlayer().getBlockPos(), SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.PLAYERS, 2.0f, 1.0f);
        world.spawnParticles(ParticleTypes.ENCHANT, context.getPlayer().getX(), context.getPlayer().getY() + 1, context.getPlayer().getZ(), 60, 0.5, 1.0, 0.5, 0.2);

        int spell = random.nextInt(4);
        switch (spell) {
            case 0 -> {
                context.getPlayer().addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 200, 2));
                context.getPlayer().addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 300, 2));
            }
            case 1 -> {
                context.getPlayer().addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 200, 2));
                context.getPlayer().addStatusEffect(new StatusEffectInstance(StatusEffects.JUMP_BOOST, 200, 2));
            }
            case 2 -> {
                context.getPlayer().addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 200, 2));
                context.getPlayer().addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 200, 1));
            }
            case 3 -> {
                context.getPlayer().addStatusEffect(new StatusEffectInstance(StatusEffects.INVISIBILITY, 300, 0));
                context.getPlayer().addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 600, 0));
            }
        }
        return true;
    }
}
