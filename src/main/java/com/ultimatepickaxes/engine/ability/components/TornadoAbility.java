package com.ultimatepickaxes.engine.ability.components;

import com.google.gson.JsonObject;
import com.ultimatepickaxes.engine.ability.AbilityComponent;
import com.ultimatepickaxes.engine.ability.AbilityContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;

public class TornadoAbility implements AbilityComponent {

    @Override
    public boolean execute(JsonObject params, AbilityContext context) {
        if (!(context.getWorld() instanceof ServerWorld world) || context.getPlayer() == null) {
            return false;
        }

        Vec3d eyePos = context.getPlayer().getEyePos();
        Vec3d look = context.getPlayer().getRotationVector();
        double distance = params.has("distance") ? params.get("distance").getAsDouble() : 8.0;
        Vec3d center = eyePos.add(look.multiply(distance));

        world.playSound(null, context.getPlayer().getBlockPos(), SoundEvents.ITEM_ELYTRA_FLYING, SoundCategory.PLAYERS, 2.0f, 0.6f);

        // Render multi-ring tornado vortex particles
        for (int y = 0; y < 10; y++) {
            double radius = 0.5 + (y * 0.4);
            for (int i = 0; i < 12; i++) {
                double angle = (i / 12.0) * Math.PI * 2 + (y * 0.5);
                double px = center.x + Math.cos(angle) * radius;
                double pz = center.z + Math.sin(angle) * radius;
                double py = center.y + (y * 0.8);

                world.spawnParticles(ParticleTypes.CLOUD, px, py, pz, 1, 0.05, 0.05, 0.05, 0.05);
                world.spawnParticles(ParticleTypes.SWEEP_ATTACK, px, py, pz, 1, 0.02, 0.02, 0.02, 0.02);
            }
        }

        // Pull nearby entities into tornado and launch them upwards
        Box box = new Box(center.add(-6, -2, -6), center.add(6, 12, 6));
        for (LivingEntity entity : world.getEntitiesByClass(LivingEntity.class, box, e -> e != context.getPlayer())) {
            entity.damage(world.getDamageSources().playerAttack(context.getPlayer()), 12.0f);
            Vec3d pull = center.subtract(entity.getPos()).normalize().multiply(0.8).add(0, 1.4, 0);
            entity.setVelocity(pull);
            entity.velocityModified = true;

            if (entity instanceof ServerPlayerEntity serverPlayer) {
                serverPlayer.networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(serverPlayer));
            }
        }

        return true;
    }
}
