package com.ultimatepickaxes.engine.ability.components;

import com.google.gson.JsonObject;
import com.ultimatepickaxes.engine.ability.AbilityComponent;
import com.ultimatepickaxes.engine.ability.AbilityContext;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;

public class StatusEffectAbility implements AbilityComponent {

    @Override
    public boolean execute(JsonObject params, AbilityContext context) {
        if (context.getPlayer() == null) return false;

        String effectStr = params.has("effectId") ? params.get("effectId").getAsString() : "minecraft:haste";
        int duration = params.has("duration") ? params.get("duration").getAsInt() : 400;
        int amplifier = params.has("amplifier") ? params.get("amplifier").getAsInt() : 1;

        RegistryEntry<StatusEffect> effectEntry = StatusEffects.HASTE;
        var optionalEntry = Registries.STATUS_EFFECT.getEntry(Identifier.of(effectStr));
        if (optionalEntry.isPresent()) {
            effectEntry = optionalEntry.get();
        }

        context.getPlayer().addStatusEffect(new StatusEffectInstance(effectEntry, duration, amplifier, true, true));

        if (context.getWorld() instanceof ServerWorld world) {
            world.playSound(null, context.getPlayer().getBlockPos(), SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.PLAYERS, 1.0f, 1.2f);
            world.spawnParticles(ParticleTypes.HAPPY_VILLAGER,
                    context.getPlayer().getX(), context.getPlayer().getY() + 1.0, context.getPlayer().getZ(),
                    20, 0.5, 0.5, 0.5, 0.1);
        }

        return true;
    }
}

