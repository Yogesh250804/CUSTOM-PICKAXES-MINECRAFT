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

public class CookieFrenzyAbility implements AbilityComponent {

    @Override
    public boolean execute(JsonObject params, AbilityContext context) {
        if (!(context.getWorld() instanceof ServerWorld world) || context.getPlayer() == null) {
            return false;
        }

        Vec3d eyePos = context.getPlayer().getEyePos();
        Vec3d look = context.getPlayer().getRotationVector();

        world.playSound(null, context.getPlayer().getBlockPos(), SoundEvents.ENTITY_GENERIC_EAT, SoundCategory.PLAYERS, 2.0f, 1.5f);

        // Gatling barrage of cookie shrapnel
        for (int i = 1; i <= 15; i++) {
            Vec3d point = eyePos.add(look.multiply(i * 1.2));
            world.spawnParticles(ParticleTypes.CRIT, point.x, point.y, point.z, 5, 0.2, 0.2, 0.2, 0.1);
            world.spawnParticles(ParticleTypes.ENCHANTED_HIT, point.x, point.y, point.z, 3, 0.2, 0.2, 0.2, 0.1);

            Box box = new Box(point.add(-1.0, -1.0, -1.0), point.add(1.0, 1.0, 1.0));
            for (LivingEntity entity : world.getEntitiesByClass(LivingEntity.class, box, e -> e != context.getPlayer())) {
                entity.damage(world.getDamageSources().playerAttack(context.getPlayer()), 8.0f);
                entity.takeKnockback(0.8, -look.x, -look.z);
            }
        }

        context.getPlayer().addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 300, 2, false, true));
        context.getPlayer().addStatusEffect(new StatusEffectInstance(StatusEffects.HASTE, 300, 1, false, true));
        context.getPlayer().getHungerManager().add(12, 0.8f);

        return true;
    }
}
