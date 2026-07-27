package com.ultimatepickaxes.engine.ability.components;

import com.google.gson.JsonObject;
import com.ultimatepickaxes.engine.ability.AbilityComponent;
import com.ultimatepickaxes.engine.ability.AbilityContext;
import net.minecraft.entity.ItemEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public class ItemMagnetAbility implements AbilityComponent {

    @Override
    public boolean execute(JsonObject params, AbilityContext context) {
        if (!(context.getWorld() instanceof ServerWorld world) || context.getPlayer() == null) {
            return false;
        }

        double radius = params.has("radius") ? params.get("radius").getAsDouble() : 8.0;
        double pullForce = params.has("pullForce") ? params.get("pullForce").getAsDouble() : 0.4;

        Vec3d playerPos = context.getPlayer().getPos();
        Box area = new Box(playerPos.add(-radius, -radius, -radius), playerPos.add(radius, radius, radius));

        boolean pulledAny = false;
        for (ItemEntity item : world.getEntitiesByClass(ItemEntity.class, area, e -> true)) {
            Vec3d dir = playerPos.subtract(item.getPos()).normalize().multiply(pullForce);
            item.setVelocity(dir);
            pulledAny = true;
        }
        return pulledAny;
    }
}
