package com.ultimatepickaxes.engine.ability.components;

import com.google.gson.JsonObject;
import com.ultimatepickaxes.engine.ability.AbilityComponent;
import com.ultimatepickaxes.engine.ability.AbilityContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public class HeadlessHorsemanAbility implements AbilityComponent {

    @Override
    public boolean execute(JsonObject params, AbilityContext context) {
        if (!(context.getWorld() instanceof ServerWorld world) || context.getPlayer() == null) {
            return false;
        }

        double distance = params.has("distance") ? params.get("distance").getAsDouble() : 20.0;
        float damage = params.has("damage") ? params.get("damage").getAsFloat() : 15.0f;

        Vec3d eyePos = context.getPlayer().getEyePos();
        Vec3d look = context.getPlayer().getRotationVector();

        world.playSound(null, context.getPlayer().getBlockPos(), SoundEvents.ENTITY_HORSE_GALLOP, SoundCategory.PLAYERS, 2.0f, 0.6f);

        for (int i = 1; i <= (int) distance; i++) {
            Vec3d point = eyePos.add(look.multiply(i));
            world.spawnParticles(ParticleTypes.FLAME, point.x, point.y, point.z, 10, 0.5, 0.5, 0.5, 0.1);
            world.spawnParticles(ParticleTypes.SOUL_FIRE_FLAME, point.x, point.y, point.z, 10, 0.5, 0.5, 0.5, 0.1);

            Box hitBox = new Box(point.add(-1.5, -1.5, -1.5), point.add(1.5, 1.5, 1.5));
            for (LivingEntity entity : world.getEntitiesByClass(LivingEntity.class, hitBox, e -> e != context.getPlayer())) {
                entity.damage(world.getDamageSources().playerAttack(context.getPlayer()), damage);
                entity.setVelocity(look.multiply(2.0).add(0, 0.6, 0));
                entity.velocityModified = true;
            }
        }
        return true;
    }
}
