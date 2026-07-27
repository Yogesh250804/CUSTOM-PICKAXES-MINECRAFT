package com.ultimatepickaxes.engine.ability.components;

import com.google.gson.JsonObject;
import com.ultimatepickaxes.engine.ability.AbilityComponent;
import com.ultimatepickaxes.engine.ability.AbilityContext;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public class SummonLightningAbility implements AbilityComponent {

    @Override
    public boolean execute(JsonObject params, AbilityContext context) {
        if (!(context.getWorld() instanceof ServerWorld world)) return false;

        BlockPos targetPos = context.getPos();
        if (targetPos == null && context.getPlayer() != null) {
            targetPos = context.getPlayer().getBlockPos();
        }
        if (targetPos == null) return false;

        LightningEntity lightning = EntityType.LIGHTNING_BOLT.create(world);
        if (lightning != null) {
            lightning.refreshPositionAfterTeleport(targetPos.getX() + 0.5, targetPos.getY(), targetPos.getZ() + 0.5);
            world.spawnEntity(lightning);
            return true;
        }
        return false;
    }
}
