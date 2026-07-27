package com.ultimatepickaxes.engine.ability.components;

import com.google.gson.JsonObject;
import com.ultimatepickaxes.engine.ability.AbilityComponent;
import com.ultimatepickaxes.engine.ability.AbilityContext;
import net.minecraft.entity.ItemEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public class ItemMagnetAbility implements AbilityComponent {

    @Override
    public boolean execute(JsonObject params, AbilityContext context) {
        if (!(context.getWorld() instanceof ServerWorld world) || context.getPlayer() == null) {
            return false;
        }

        double radius = params.has("radius") ? params.get("radius").getAsDouble() : 16.0;

        Vec3d playerPos = context.getPlayer().getPos();
        Box area = new Box(playerPos.add(-radius, -radius, -radius), playerPos.add(radius, radius, radius));

        boolean pulledAny = false;
        for (ItemEntity item : world.getEntitiesByClass(ItemEntity.class, area, e -> true)) {
            item.setPosition(playerPos.x, playerPos.y + 0.5, playerPos.z);
            item.setPickupDelay(0);
            item.velocityModified = true;
            pulledAny = true;
        }

        if (pulledAny) {
            world.playSound(null, context.getPlayer().getBlockPos(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 1.0f, 1.2f);
        }
        return true;
    }
}
