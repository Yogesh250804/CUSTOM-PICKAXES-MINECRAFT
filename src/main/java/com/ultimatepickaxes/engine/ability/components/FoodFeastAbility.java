package com.ultimatepickaxes.engine.ability.components;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ultimatepickaxes.engine.ability.AbilityComponent;
import com.ultimatepickaxes.engine.ability.AbilityContext;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.particle.ParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;

public class FoodFeastAbility implements AbilityComponent {

    @Override
    public boolean execute(JsonObject params, AbilityContext context) {
        if (context.getPlayer() == null) return false;

        int hunger = params.has("hunger") ? params.get("hunger").getAsInt() : 20;
        float saturation = params.has("saturation") ? params.get("saturation").getAsFloat() : 1.0f;

        context.getPlayer().getHungerManager().add(hunger, saturation);

        if (params.has("effects")) {
            JsonArray effects = params.getAsJsonArray("effects");
            for (JsonElement elem : effects) {
                if (elem.isJsonObject()) {
                    JsonObject obj = elem.getAsJsonObject();
                    String effectId = obj.has("id") ? obj.get("id").getAsString() : "minecraft:regeneration";
                    int duration = obj.has("duration") ? obj.get("duration").getAsInt() : 300;
                    int amp = obj.has("amplifier") ? obj.get("amplifier").getAsInt() : 0;

                    var optionalEntry = Registries.STATUS_EFFECT.getEntry(Identifier.of(effectId));
                    if (optionalEntry.isPresent()) {
                        context.getPlayer().addStatusEffect(new StatusEffectInstance(optionalEntry.get(), duration, amp, false, true));
                    }
                }
            }
        }

        if (context.getWorld() instanceof ServerWorld serverWorld) {
            serverWorld.playSound(null, context.getPlayer().getBlockPos(), SoundEvents.ENTITY_PLAYER_BURP, SoundCategory.PLAYERS, 1.5f, 1.0f);
            serverWorld.spawnParticles(ParticleTypes.HAPPY_VILLAGER,
                    context.getPlayer().getX(), context.getPlayer().getY() + 1.0, context.getPlayer().getZ(),
                    25, 0.5, 0.5, 0.5, 0.1);
        }

        return true;
    }
}
