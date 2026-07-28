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
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public class CarnivoreRageAbility implements AbilityComponent {

    @Override
    public boolean execute(JsonObject params, AbilityContext context) {
        if (!(context.getWorld() instanceof ServerWorld world) || context.getPlayer() == null) {
            return false;
        }

        Vec3d center = context.getPlayer().getPos();
        world.playSound(null, context.getPlayer().getBlockPos(), SoundEvents.ENTITY_ENDER_DRAGON_GROWL, SoundCategory.PLAYERS, 2.5f, 0.9f);
        world.spawnParticles(ParticleTypes.EXPLOSION_EMITTER, center.x, center.y + 1, center.z, 2, 0.5, 0.5, 0.5, 0.1);
        world.spawnParticles(ParticleTypes.FLAME, center.x, center.y + 1, center.z, 80, 4.0, 1.0, 4.0, 0.2);

        // Predator roar knocks back and cooks surrounding mobs
        Box searchBox = new Box(context.getPlayer().getBlockPos()).expand(10.0);
        for (LivingEntity entity : world.getEntitiesByClass(LivingEntity.class, searchBox, e -> e != context.getPlayer())) {
            Vec3d knockbackDir = entity.getPos().subtract(center).normalize().multiply(2.0);
            entity.addVelocity(knockbackDir.x, 0.8, knockbackDir.z);
            entity.velocityModified = true;
            entity.setOnFireFor(8);
            entity.damage(world.getDamageSources().inFire(), 14.0f);
        }

        context.getPlayer().addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 600, 2, false, true));
        context.getPlayer().addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 600, 2, false, true));
        context.getPlayer().addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 600, 0, false, true));
        context.getPlayer().getHungerManager().add(20, 1.0f);

        return true;
    }
}
