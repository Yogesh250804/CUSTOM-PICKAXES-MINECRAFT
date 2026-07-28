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

public class WardenNukeAbility implements AbilityComponent {

    @Override
    public boolean execute(JsonObject params, AbilityContext context) {
        if (!(context.getWorld() instanceof ServerWorld world) || context.getPlayer() == null) {
            return false;
        }

        double radius = params.has("radius") ? params.get("radius").getAsDouble() : 16.0;
        float damage = params.has("damage") ? params.get("damage").getAsFloat() : 25.0f;

        BlockPos pos = context.getPlayer().getBlockPos();
        Vec3d center = context.getPlayer().getPos();

        world.playSound(null, pos, SoundEvents.ENTITY_WARDEN_SONIC_BOOM, SoundCategory.PLAYERS, 3.0f, 0.8f);
        world.playSound(null, pos, SoundEvents.ENTITY_WARDEN_ROAR, SoundCategory.PLAYERS, 2.5f, 0.9f);

        // Sculk Nuke Shockwave Particles
        world.spawnParticles(ParticleTypes.EXPLOSION_EMITTER, center.x, center.y + 1, center.z, 3, 0.5, 0.5, 0.5, 0.1);
        world.spawnParticles(ParticleTypes.SCULK_SOUL, center.x, center.y + 1, center.z, 120, radius / 2, 2.0, radius / 2, 0.2);

        for (int i = 0; i < 360; i += 15) {
            double rad = Math.toRadians(i);
            double dx = Math.cos(rad);
            double dz = Math.sin(rad);
            for (int r = 1; r <= (int) radius; r++) {
                world.spawnParticles(ParticleTypes.SONIC_BOOM, center.x + dx * r, center.y + 1.0, center.z + dz * r, 1, 0, 0, 0, 0);
            }
        }

        Box box = new Box(pos).expand(radius);
        for (LivingEntity entity : world.getEntitiesByClass(LivingEntity.class, box, e -> e != context.getPlayer())) {
            entity.damage(world.getDamageSources().playerAttack(context.getPlayer()), damage);
            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.DARKNESS, 200, 0, false, true));
            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 200, 3, false, true));
            Vec3d knockback = entity.getPos().subtract(center).normalize().multiply(2.5).add(0, 0.5, 0);
            entity.setVelocity(knockback);
            entity.velocityModified = true;
        }

        context.getPlayer().addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 600, 3, false, true));
        context.getPlayer().addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 400, 1, false, true));

        return true;
    }
}
