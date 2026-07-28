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

public class BrickBarrageAbility implements AbilityComponent {

    @Override
    public boolean execute(JsonObject params, AbilityContext context) {
        if (!(context.getWorld() instanceof ServerWorld world) || context.getPlayer() == null) {
            return false;
        }

        int count = params.has("count") ? params.get("count").getAsInt() : 10;
        float damage = params.has("damage") ? params.get("damage").getAsFloat() : 3.0f;
        double range = params.has("range") ? params.get("range").getAsDouble() : 14.0;

        Vec3d eyePos = context.getPlayer().getEyePos();
        Vec3d look = context.getPlayer().getRotationVector();

        world.playSound(null, context.getPlayer().getBlockPos(), SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.PLAYERS, 1.5f, 0.6f);

        for (int i = 0; i < count; i++) {
            double spreadX = (world.getRandom().nextDouble() - 0.5) * 0.2;
            double spreadY = (world.getRandom().nextDouble() - 0.5) * 0.2;
            double spreadZ = (world.getRandom().nextDouble() - 0.5) * 0.2;
            Vec3d dir = look.add(spreadX, spreadY, spreadZ).normalize();

            for (int d = 1; d <= (int) range; d++) {
                Vec3d point = eyePos.add(dir.multiply(d));
                world.spawnParticles(ParticleTypes.CRIT, point.x, point.y, point.z, 1, 0, 0, 0, 0);

                Box hitBox = new Box(point.add(-0.5, -0.5, -0.5), point.add(0.5, 0.5, 0.5));
                for (LivingEntity entity : world.getEntitiesByClass(LivingEntity.class, hitBox, e -> e != context.getPlayer())) {
                    entity.damage(world.getDamageSources().playerAttack(context.getPlayer()), damage);
                    entity.setVelocity(dir.multiply(0.4).add(0, 0.2, 0));
                    entity.velocityModified = true;
                    break;
                }
            }
        }
        return true;
    }
}
