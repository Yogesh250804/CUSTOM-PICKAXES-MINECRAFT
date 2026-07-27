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

public class SonicBoomAbility implements AbilityComponent {

    @Override
    public boolean execute(JsonObject params, AbilityContext context) {
        if (!(context.getWorld() instanceof ServerWorld world) || context.getPlayer() == null) {
            return false;
        }

        double range = params.has("range") ? params.get("range").getAsDouble() : 16.0;
        float damage = params.has("damage") ? params.get("damage").getAsFloat() : 12.0f;

        Vec3d eyePos = context.getPlayer().getEyePos();
        Vec3d look = context.getPlayer().getRotationVector();
        Vec3d target = eyePos.add(look.multiply(range));

        world.playSound(null, context.getPlayer().getBlockPos(), SoundEvents.ENTITY_WARDEN_SONIC_BOOM, SoundCategory.PLAYERS, 2.0f, 1.0f);

        for (int i = 1; i <= (int) range; i++) {
            Vec3d point = eyePos.add(look.multiply(i));
            world.spawnParticles(ParticleTypes.SONIC_BOOM, point.x, point.y, point.z, 1, 0, 0, 0, 0);
        }

        Box box = new Box(eyePos, target).expand(2.0);
        for (LivingEntity entity : world.getEntitiesByClass(LivingEntity.class, box, e -> e != context.getPlayer())) {
            entity.damage(world.getDamageSources().playerAttack(context.getPlayer()), damage);
            entity.setVelocity(look.multiply(2.0).add(0, 0.4, 0));
            entity.velocityModified = true;
        }
        return true;
    }
}
