package com.ultimatepickaxes.engine.ability.components;

import com.google.gson.JsonObject;
import com.ultimatepickaxes.engine.ability.AbilityComponent;
import com.ultimatepickaxes.engine.ability.AbilityContext;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;

public class AreaMiningAbility implements AbilityComponent {

    @Override
    public boolean execute(JsonObject params, AbilityContext context) {
        if (!(context.getWorld() instanceof ServerWorld world) || context.getPlayer() == null) {
            return false;
        }

        int radius = params.has("radius") ? params.get("radius").getAsInt() : 1;
        BlockPos center = context.getPos();

        if (center == null) {
            HitResult hit = context.getPlayer().raycast(5.0, 0.0f, false);
            if (hit instanceof BlockHitResult blockHit) {
                center = blockHit.getBlockPos();
            } else {
                center = context.getPlayer().getBlockPos().offset(context.getPlayer().getHorizontalFacing(), 2);
            }
        }

        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    BlockPos targetPos = center.add(x, y, z);
                    BlockState state = world.getBlockState(targetPos);
                    if (!state.isAir() && state.getHardness(world, targetPos) >= 0) {
                        world.breakBlock(targetPos, true, context.getPlayer());
                    }
                }
            }
        }
        return true;
    }
}
