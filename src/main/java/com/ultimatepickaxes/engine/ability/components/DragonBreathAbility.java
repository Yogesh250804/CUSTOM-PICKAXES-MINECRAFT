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

public class DragonBreathAbility implements AbilityComponent {

    @Override
    public boolean execute(JsonObject params, AbilityContext context) {
        if (!(context.getWorld() instanceof ServerWorld world) || context.getPlayer() == null) {
            return false;
        }

        double range = params.has("range") ? params.get("range").getAsDouble() : 12.0;
        float damage = params.has("damage") ? params.get("damage").getAsFloat() : 16.0f;

        Vec3d eyePos = context.getPlayer().getEyePos();
        Vec3d look = context.getPlayer().getRotationVector();

        world.playSound(null, context.getPlayer().getBlockPos(), SoundEvents.ENTITY_ENDER_DRAGON_GROWL, SoundCategory.PLAYERS, 2.0f, 1.0f);
        context.getPlayer().addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, 200, 0, false, false));

        for (int i = 1; i <= (int) range; i++) {
            Vec3d point = eyePos.add(look.multiply(i));
            world.spawnParticles(ParticleTypes.DRAGON_BREATH, point.x, point.y, point.z, 20, 0.5, 0.5, 0.5, 0.05);

            Box hitBox = new Box(point.add(-1.5, -1.5, -1.5), point.add(1.5, 1.5, 1.5));
            for (LivingEntity entity : world.getEntitiesByClass(LivingEntity.class, hitBox, e -> e != context.getPlayer())) {
                entity.damage(world.getDamageSources().dragonBreath(), damage);
            }
        }
        return true;
    }
}
