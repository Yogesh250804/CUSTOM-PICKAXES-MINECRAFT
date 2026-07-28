package com.ultimatepickaxes.engine.ability.components;

import com.google.gson.JsonObject;
import com.ultimatepickaxes.engine.ability.AbilityComponent;
import com.ultimatepickaxes.engine.ability.AbilityContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.SmallFireballEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public class BoneBarrageAbility implements AbilityComponent {

    @Override
    public boolean execute(JsonObject params, AbilityContext context) {
        if (!(context.getWorld() instanceof ServerWorld world) || context.getPlayer() == null) {
            return false;
        }

        int projectileCount = params.has("count") ? params.get("count").getAsInt() : 8;
        float damage = params.has("damage") ? params.get("damage").getAsFloat() : 4.0f;
        double range = params.has("range") ? params.get("range").getAsDouble() : 16.0;

        Vec3d eyePos = context.getPlayer().getEyePos();
        Vec3d look = context.getPlayer().getRotationVector();

        world.playSound(null, context.getPlayer().getBlockPos(), SoundEvents.ENTITY_SKELETON_SHOOT, SoundCategory.PLAYERS, 1.5f, 1.2f);

        // Fire multiple projectiles in a spread pattern
        for (int i = 0; i < projectileCount; i++) {
            double spreadX = (world.getRandom().nextDouble() - 0.5) * 0.3;
            double spreadY = (world.getRandom().nextDouble() - 0.5) * 0.3;
            double spreadZ = (world.getRandom().nextDouble() - 0.5) * 0.3;

            Vec3d direction = look.add(spreadX, spreadY, spreadZ).normalize();

            // Spawn particles along the projectile path and damage entities
            for (int d = 1; d <= (int) range; d++) {
                Vec3d point = eyePos.add(direction.multiply(d));
                world.spawnParticles(ParticleTypes.CRIT, point.x, point.y, point.z, 1, 0, 0, 0, 0);

                // Check for entity hits at this point
                Box hitBox = new Box(point.add(-0.5, -0.5, -0.5), point.add(0.5, 0.5, 0.5));
                for (LivingEntity entity : world.getEntitiesByClass(LivingEntity.class, hitBox, e -> e != context.getPlayer())) {
                    entity.damage(world.getDamageSources().playerAttack(context.getPlayer()), damage);
                    entity.setVelocity(direction.multiply(0.5).add(0, 0.3, 0));
                    entity.velocityModified = true;
                    break; // Hit one entity per projectile
                }
            }
        }

        return true;
    }
}
