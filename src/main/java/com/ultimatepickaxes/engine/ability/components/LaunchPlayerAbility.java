package com.ultimatepickaxes.engine.ability.components;

import com.google.gson.JsonObject;
import com.ultimatepickaxes.engine.ability.AbilityComponent;
import com.ultimatepickaxes.engine.ability.AbilityContext;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;

public class LaunchPlayerAbility implements AbilityComponent {

    @Override
    public boolean execute(JsonObject params, AbilityContext context) {
        if (context.getPlayer() == null) return false;

        double upwardForce = params.has("upwardForce") ? params.get("upwardForce").getAsDouble() : 1.4;
        double forwardForce = params.has("forwardForce") ? params.get("forwardForce").getAsDouble() : 0.8;

        Vec3d look = context.getPlayer().getRotationVector().multiply(forwardForce);
        Vec3d vel = new Vec3d(look.x, upwardForce, look.z);

        context.getPlayer().setVelocity(vel);
        context.getPlayer().velocityModified = true;
        context.getPlayer().onLanding();

        if (context.getPlayer() instanceof ServerPlayerEntity serverPlayer) {
            serverPlayer.networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(serverPlayer));
        }
        return true;
    }
}
