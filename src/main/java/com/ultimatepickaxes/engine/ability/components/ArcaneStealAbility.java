package com.ultimatepickaxes.engine.ability.components;

import com.google.gson.JsonObject;
import com.ultimatepickaxes.engine.ability.AbilityComponent;
import com.ultimatepickaxes.engine.ability.AbilityContext;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;

public class ArcaneStealAbility implements AbilityComponent {

    @Override
    public boolean execute(JsonObject params, AbilityContext context) {
        if (!(context.getWorld() instanceof ServerWorld world) || context.getPlayer() == null) {
            return false;
        }

        int xpAmount = params.has("xp") ? params.get("xp").getAsInt() : 30;

        world.playSound(null, context.getPlayer().getBlockPos(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 2.0f, 1.2f);
        world.spawnParticles(ParticleTypes.ENCHANT, context.getPlayer().getX(), context.getPlayer().getY() + 1, context.getPlayer().getZ(), 40, 0.5, 0.5, 0.5, 0.1);

        context.getPlayer().addStatusEffect(new StatusEffectInstance(StatusEffects.LUCK, 400, 1, false, true));
        ExperienceOrbEntity.spawn(world, context.getPlayer().getPos(), xpAmount);
        return true;
    }
}
