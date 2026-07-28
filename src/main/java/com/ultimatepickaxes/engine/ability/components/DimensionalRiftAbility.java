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

public class DimensionalRiftAbility implements AbilityComponent {

    @Override
    public boolean execute(JsonObject params, AbilityContext context) {
        if (!(context.getWorld() instanceof ServerWorld world) || context.getPlayer() == null) {
            return false;
        }

        Vec3d eyePos = context.getPlayer().getEyePos();
        Vec3d look = context.getPlayer().getRotationVector();
        Vec3d riftPos = eyePos.add(look.multiply(6));

        world.playSound(null, context.getPlayer().getBlockPos(), SoundEvents.BLOCK_PORTAL_TRAVEL, SoundCategory.PLAYERS, 1.5f, 1.2f);
        world.playSound(null, context.getPlayer().getBlockPos(), SoundEvents.ENTITY_ENDER_DRAGON_GROWL, SoundCategory.PLAYERS, 1.0f, 0.8f);

        // Render dimensional rift tears
        for (int i = 0; i < 200; i++) {
            double rx = (Math.random() - 0.5) * 3.0;
            double ry = (Math.random() - 0.5) * 5.0;
            double rz = (Math.random() - 0.5) * 3.0;

            world.spawnParticles(ParticleTypes.PORTAL, riftPos.x + rx, riftPos.y + ry, riftPos.z + rz, 1, 0.1, 0.1, 0.1, 0.2);
            world.spawnParticles(ParticleTypes.DRAGON_BREATH, riftPos.x + rx, riftPos.y + ry, riftPos.z + rz, 1, 0.05, 0.05, 0.05, 0.05);
            world.spawnParticles(ParticleTypes.SOUL_FIRE_FLAME, riftPos.x + rx, riftPos.y + ry, riftPos.z + rz, 1, 0.05, 0.05, 0.05, 0.05);
        }

        // Teleport all surrounding mobs 100 blocks up into the sky to fall
        Box box = new Box(riftPos.add(-8, -5, -8), riftPos.add(8, 5, 8));
        for (LivingEntity entity : world.getEntitiesByClass(LivingEntity.class, box, e -> e != context.getPlayer())) {
            entity.damage(world.getDamageSources().magic(), 20.0f);
            Vec3d highPos = entity.getPos().add(0, 80, 0);
            entity.requestTeleport(highPos.x, highPos.y, highPos.z);
            entity.setVelocity(0, -1.0, 0);
            entity.velocityModified = true;

            if (entity instanceof ServerPlayerEntity serverPlayer) {
                serverPlayer.networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(serverPlayer));
            }
        }

        return true;
    }
}
