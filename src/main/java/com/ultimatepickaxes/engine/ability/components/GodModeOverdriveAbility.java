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
import net.minecraft.util.math.Vec3d;

public class GodModeOverdriveAbility implements AbilityComponent {

    @Override
    public boolean execute(JsonObject params, AbilityContext context) {
        if (!(context.getWorld() instanceof ServerWorld world) || context.getPlayer() == null) {
            return false;
        }

        int duration = params.has("duration") ? params.get("duration").getAsInt() : 600;
        BlockPos pos = context.getPlayer().getBlockPos();
        Vec3d center = context.getPlayer().getPos();

        world.playSound(null, pos, SoundEvents.UI_TOAST_CHALLENGE_COMPLETE, SoundCategory.PLAYERS, 2.5f, 1.0f);
        world.playSound(null, pos, SoundEvents.BLOCK_END_PORTAL_SPAWN, SoundCategory.PLAYERS, 2.0f, 1.2f);

        world.spawnParticles(ParticleTypes.FLASH, center.x, center.y + 1.5, center.z, 3, 0.5, 0.5, 0.5, 0.1);
        world.spawnParticles(ParticleTypes.END_ROD, center.x, center.y + 1.0, center.z, 150, 2.0, 2.0, 2.0, 0.2);
        world.spawnParticles(ParticleTypes.REVERSE_PORTAL, center.x, center.y + 1.0, center.z, 100, 3.0, 3.0, 3.0, 0.15);

        // Ultimate Overdrive God Mode Status Effects
        context.getPlayer().addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, duration, 4, false, true));
        context.getPlayer().addStatusEffect(new StatusEffectInstance(StatusEffects.HASTE, duration, 4, false, true));
        context.getPlayer().addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, duration, 3, false, true));
        context.getPlayer().addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, duration, 4, false, true));
        context.getPlayer().addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, duration, 4, false, true));
        context.getPlayer().addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, duration * 2, 9, false, true));
        context.getPlayer().addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, duration * 2, 0, false, true));
        context.getPlayer().addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, duration * 2, 0, false, true));
        context.getPlayer().addStatusEffect(new StatusEffectInstance(StatusEffects.SATURATION, duration, 1, false, true));
        context.getPlayer().getHungerManager().add(20, 1.0f);

        // Cosmic blast smites hostile entities around player
        Box box = new Box(pos).expand(20.0);
        for (LivingEntity entity : world.getEntitiesByClass(LivingEntity.class, box, e -> e != context.getPlayer())) {
            entity.damage(world.getDamageSources().magic(), 35.0f);
            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, duration, 0, false, true));
            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 200, 255, false, true));
        }

        return true;
    }
}
