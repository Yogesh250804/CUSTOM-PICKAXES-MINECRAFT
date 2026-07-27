package com.ultimatepickaxes.engine.ability.components;

import com.google.gson.JsonObject;
import com.ultimatepickaxes.engine.ability.AbilityComponent;
import com.ultimatepickaxes.engine.ability.AbilityContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public class PushEntitiesAbility implements AbilityComponent {

    @Override
    public boolean execute(JsonObject params, AbilityContext context) {
        if (!(context.getWorld() instanceof ServerWorld world) || context.getPlayer() == null) {
            return false;
        }

        double radius = params.has("radius") ? params.get("radius").getAsDouble() : 6.0;
        double force = params.has("force") ? params.get("force").getAsDouble() : 1.5;

        Vec3d center = context.getPlayer().getPos();
        Vec3d look = context.getPlayer().getRotationVector();
        Box area = new Box(center.x - radius, center.y - radius, center.z - radius, center.x + radius, center.y + radius, center.z + radius);

        boolean hitAny = false;
        for (LivingEntity entity : world.getEntitiesByClass(LivingEntity.class, area, e -> e != context.getPlayer())) {
            Vec3d diff = entity.getPos().subtract(center);
            Vec3d push = (diff.lengthSquared() > 0.01 ? diff.normalize() : look).multiply(force).add(0, 0.6, 0);
            
            entity.setVelocity(push);
            entity.velocityModified = true;
            entity.damage(world.getDamageSources().playerAttack(context.getPlayer()), 4.0f);

            if (entity instanceof ServerPlayerEntity serverPlayer) {
                serverPlayer.networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(serverPlayer));
            }
            hitAny = true;
        }
        return true;
    }
}
