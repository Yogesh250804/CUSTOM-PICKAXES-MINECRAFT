package com.ultimatepickaxes.engine.ability.components;

import com.google.gson.JsonObject;
import com.ultimatepickaxes.engine.ability.AbilityComponent;
import com.ultimatepickaxes.engine.ability.AbilityContext;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class AreaMiningAbility implements AbilityComponent {

    @Override
    public boolean execute(JsonObject params, AbilityContext context) {
        if (!(context.getWorld() instanceof ServerWorld world) || context.getPlayer() == null) {
            return false;
        }

        BlockPos center = context.getPos();
        if (center == null) {
            HitResult hit = context.getPlayer().raycast(5.0, 0.0f, false);
            if (hit instanceof BlockHitResult blockHit) {
                center = blockHit.getBlockPos();
            } else {
                center = context.getPlayer().getBlockPos().offset(context.getPlayer().getHorizontalFacing(), 2);
            }
        }

        // Support both radius-based (sphere) and width/height/depth (oriented rectangle)
        if (params.has("width") || params.has("height") || params.has("depth")) {
            int width = params.has("width") ? params.get("width").getAsInt() : 1;
            int height = params.has("height") ? params.get("height").getAsInt() : 1;
            int depth = params.has("depth") ? params.get("depth").getAsInt() : 1;

            Direction facing = context.getPlayer().getHorizontalFacing();
            Direction right = facing.rotateYClockwise();
            int halfW = width / 2;
            int halfH = height / 2;

            for (int d = 0; d < depth; d++) {
                for (int w = -halfW; w <= halfW; w++) {
                    for (int h = -halfH; h <= halfH; h++) {
                        BlockPos targetPos = center
                                .offset(facing, d)
                                .offset(right, w)
                                .offset(Direction.UP, h);
                        BlockState state = world.getBlockState(targetPos);
                        if (!state.isAir() && state.getHardness(world, targetPos) >= 0
                                && !targetPos.equals(center)) {
                            world.breakBlock(targetPos, true, context.getPlayer());
                        }
                    }
                }
            }
        } else {
            int radius = params.has("radius") ? params.get("radius").getAsInt() : 1;
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
        }
        return true;
    }
}
