package com.ultimatepickaxes.engine.ability.components;

import com.google.gson.JsonObject;
import com.ultimatepickaxes.engine.ability.AbilityComponent;
import com.ultimatepickaxes.engine.ability.AbilityContext;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class AreaMiningAbility implements AbilityComponent {

    @Override
    public boolean execute(JsonObject params, AbilityContext context) {
        if (!(context.getWorld() instanceof ServerWorld world) || context.getPos() == null || context.getPlayer() == null) {
            return false;
        }

        int width = params.has("width") ? params.get("width").getAsInt() : 3;
        int height = params.has("height") ? params.get("height").getAsInt() : 3;
        int depth = params.has("depth") ? params.get("depth").getAsInt() : 1;

        Direction side = context.getSide();
        if (side == null) side = context.getPlayer().getHorizontalFacing().getOpposite();

        BlockPos center = context.getPos();
        int halfW = width / 2;
        int halfH = height / 2;

        for (int x = -halfW; x <= halfW; x++) {
            for (int y = -halfH; y <= halfH; y++) {
                for (int z = 0; z < depth; z++) {
                    if (x == 0 && y == 0 && z == 0) continue;

                    BlockPos targetPos;
                    if (side.getAxis() == Direction.Axis.Y) {
                        targetPos = center.add(x, 0, y);
                    } else if (side.getAxis() == Direction.Axis.X) {
                        targetPos = center.add(z * side.getOffsetX(), y, x);
                    } else {
                        targetPos = center.add(x, y, z * side.getOffsetZ());
                    }

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
