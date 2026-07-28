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

public class GuardianBeamAbility implements AbilityComponent {

    @Override
    public boolean execute(JsonObject params, AbilityContext context) {
        if (!(context.getWorld() instanceof ServerWorld world) || context.getPlayer() == null) {
            return false;
        }

        double range = params.has("range") ? params.get("range").getAsDouble() : 18.0;
        float damage = params.has("damage") ? params.get("damage").getAsFloat() : 14.0f;

        Vec3d eyePos = context.getPlayer().getEyePos();
        Vec3d look = context.getPlayer().getRotationVector();

        world.playSound(null, context.getPlayer().getBlockPos(), SoundEvents.ENTITY_ELDER_GUARDIAN_CURSE, SoundCategory.PLAYERS, 1.5f, 1.2f);

        for (int i = 1; i <= (int) range; i++) {
            Vec3d point = eyePos.add(look.multiply(i));
            world.spawnParticles(ParticleTypes.BUBBLE, point.x, point.y, point.z, 5, 0.1, 0.1, 0.1, 0.01);
            world.spawnParticles(ParticleTypes.GLOW, point.x, point.y, point.z, 2, 0.1, 0.1, 0.1, 0.01);

            Box hitBox = new Box(point.add(-1.0, -1.0, -1.0), point.add(1.0, 1.0, 1.0));
            for (LivingEntity entity : world.getEntitiesByClass(LivingEntity.class, hitBox, e -> e != context.getPlayer())) {
                entity.damage(world.getDamageSources().indirectMagic(context.getPlayer(), context.getPlayer()), damage);
            }
        }
        return true;
    }
}
