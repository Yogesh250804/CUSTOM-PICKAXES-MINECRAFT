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

public class SolarFlareAbility implements AbilityComponent {

    @Override
    public boolean execute(JsonObject params, AbilityContext context) {
        if (!(context.getWorld() instanceof ServerWorld world) || context.getPlayer() == null) {
            return false;
        }

        Vec3d eyePos = context.getPlayer().getEyePos();
        Vec3d look = context.getPlayer().getRotationVector();
        Vec3d sunPos = eyePos.add(look.multiply(8)).add(0, 8, 0);

        world.playSound(null, context.getPlayer().getBlockPos(), SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.PLAYERS, 2.5f, 0.5f);
        world.playSound(null, context.getPlayer().getBlockPos(), SoundEvents.ENTITY_GENERIC_EXPLODE.value(), SoundCategory.PLAYERS, 2.0f, 1.2f);

        // Render Miniature Sun sphere
        for (int i = 0; i < 150; i++) {
            double u = Math.random();
            double v = Math.random();
            double theta = u * 2.0 * Math.PI;
            double phi = Math.acos(2.0 * v - 1.0);
            double r = 2.5;
            double x = sunPos.x + r * Math.sin(phi) * Math.cos(theta);
            double y = sunPos.y + r * Math.sin(phi) * Math.sin(theta);
            double z = sunPos.z + r * Math.cos(phi);

            world.spawnParticles(ParticleTypes.FLAME, x, y, z, 1, 0, 0, 0, 0.05);
            world.spawnParticles(ParticleTypes.END_ROD, x, y, z, 1, 0, 0, 0, 0.02);
            world.spawnParticles(ParticleTypes.FIREWORK, x, y, z, 1, 0, 0, 0, 0.1);
        }

        // Blind, glow, and rain solar beams onto all mobs in 20 block radius
        Box box = new Box(sunPos.add(-20, -15, -20), sunPos.add(20, 15, 20));
        for (LivingEntity entity : world.getEntitiesByClass(LivingEntity.class, box, e -> e != context.getPlayer())) {
            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 160, 1, true, true));
            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 160, 1, true, true));
            entity.damage(world.getDamageSources().onFire(), 25.0f);
            entity.setOnFireFor(12);

            // Solar strike beam on each entity
            Vec3d ePos = entity.getPos();
            for (double h = ePos.y; h < ePos.y + 12; h += 0.5) {
                world.spawnParticles(ParticleTypes.END_ROD, ePos.x, h, ePos.z, 2, 0.1, 0.1, 0.1, 0.01);
            }
        }

        return true;
    }
}
