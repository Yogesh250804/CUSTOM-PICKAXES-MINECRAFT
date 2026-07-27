package com.ultimatepickaxes.engine.ability.components;

import com.google.gson.JsonObject;
import com.ultimatepickaxes.engine.ability.AbilityComponent;
import com.ultimatepickaxes.engine.ability.AbilityContext;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

public class StatusEffectAbility implements AbilityComponent {

    @Override
    public boolean execute(JsonObject params, AbilityContext context) {
        if (context.getPlayer() == null) return false;

        String effectStr = params.has("effectId") ? params.get("effectId").getAsString() : "minecraft:haste";
        @SuppressWarnings("unchecked")
        RegistryEntry<StatusEffect> effectEntry = Registries.STATUS_EFFECT.getEntry(Identifier.of(effectStr))
                .map(e -> (RegistryEntry<StatusEffect>) e)
                .orElse((RegistryEntry<StatusEffect>) (Object) StatusEffects.HASTE);

        int duration = params.has("duration") ? params.get("duration").getAsInt() : 200;
        int amplifier = params.has("amplifier") ? params.get("amplifier").getAsInt() : 0;

        context.getPlayer().addStatusEffect(new StatusEffectInstance(effectEntry, duration, amplifier, true, true));
        return true;
    }
}
