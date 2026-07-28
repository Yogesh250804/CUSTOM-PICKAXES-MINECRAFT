package com.ultimatepickaxes.engine.ability.components;

import com.google.gson.JsonObject;
import com.ultimatepickaxes.engine.ability.AbilityComponent;
import com.ultimatepickaxes.engine.ability.AbilityContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public class GravityWellAbility implements AbilityComponent {

    @Override
    public boolean execute(JsonObject params, AbilityContext context) {
        if (!(context.getWorld() instanceof ServerWorld world) || context.getPlayer() == null) {
            return false;
        }

        double radius = params.has("radius") ? params.get("radius").getAsDouble() : 10.0;
        float damage = params.has("damage") ? params.get("damage").getAsFloat() : 12.0f;

        HitResult hit = context.getPlayer().raycast(16.0, 0.0f, false);
        Vec3d center = hit.getPos();

        world.playSound(null, BlockPos.ofFloored(center), SoundEvents.BLOCK_PORTAL_TRAVEL, SoundCategory.PLAYERS, 1.5f, 0.5f);
        world.spawnParticles(ParticleTypes.REVERSE_PORTAL, center.x, center.y + 1, center.z, 100, radius / 2.0, 1.0, radius / 2.0, 0.2);

        Box area = new Box(center.x - radius, center.y - radius, center.z - radius, center.x + radius, center.y + radius, center.z + radius);
        for (LivingEntity entity : world.getEntitiesByClass(LivingEntity.class, area, e -> e != context.getPlayer())) {
            Vec3d diff = center.subtract(entity.getPos()).normalize().multiply(1.8);
            entity.setVelocity(diff);
            entity.velocityModified = true;
            entity.damage(world.getDamageSources().magic(), damage);
        }
        return true;
    }
}
