package com.ultimatepickaxes.engine.ability.components;

import com.google.gson.JsonObject;
import com.ultimatepickaxes.engine.ability.AbilityComponent;
import com.ultimatepickaxes.engine.ability.AbilityContext;
import net.minecraft.util.math.Vec3d;

public class LaunchPlayerAbility implements AbilityComponent {

    @Override
    public boolean execute(JsonObject params, AbilityContext context) {
        if (context.getPlayer() == null) return false;

        double upwardForce = params.has("upwardForce") ? params.get("upwardForce").getAsDouble() : 1.2;
        double forwardForce = params.has("forwardForce") ? params.get("forwardForce").getAsDouble() : 0.5;

        Vec3d look = context.getPlayer().getRotationVector().multiply(forwardForce);
        context.getPlayer().setVelocity(look.x, upwardForce, look.z);
        context.getPlayer().velocityModified = true;
        return true;
    }
}
