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

public class MelonCannonadeAbility implements AbilityComponent {

    @Override
    public boolean execute(JsonObject params, AbilityContext context) {
        if (!(context.getWorld() instanceof ServerWorld world) || context.getPlayer() == null) {
            return false;
        }

        Vec3d eyePos = context.getPlayer().getEyePos();
        Vec3d look = context.getPlayer().getRotationVector();

        world.playSound(null, context.getPlayer().getBlockPos(), SoundEvents.ENTITY_DRAGON_FIREBALL_EXPLODE, SoundCategory.PLAYERS, 2.0f, 1.4f);

        // Explosive melon seed cannonade
        for (int i = 1; i <= 10; i++) {
            Vec3d point = eyePos.add(look.multiply(i * 1.5));
            world.spawnParticles(ParticleTypes.EXPLOSION, point.x, point.y, point.z, 2, 0.3, 0.3, 0.3, 0.1);
            world.spawnParticles(ParticleTypes.ITEM_SLIME, point.x, point.y, point.z, 15, 0.4, 0.4, 0.4, 0.1);

            Box box = new Box(point.add(-1.5, -1.5, -1.5), point.add(1.5, 1.5, 1.5));
            for (LivingEntity entity : world.getEntitiesByClass(LivingEntity.class, box, e -> e != context.getPlayer())) {
                entity.damage(world.getDamageSources().playerAttack(context.getPlayer()), 12.0f);
                entity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 100, 2, false, true));
            }
        }

        context.getPlayer().addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 300, 1, false, true));
        context.getPlayer().getHungerManager().add(12, 0.8f);

        return true;
    }
}
