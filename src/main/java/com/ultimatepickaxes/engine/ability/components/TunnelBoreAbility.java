package com.ultimatepickaxes.engine.ability.components;

import com.google.gson.JsonObject;
import com.ultimatepickaxes.engine.ability.AbilityComponent;
import com.ultimatepickaxes.engine.ability.AbilityContext;
import net.minecraft.block.BlockState;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class TunnelBoreAbility implements AbilityComponent {

    @Override
    public boolean execute(JsonObject params, AbilityContext context) {
        if (!(context.getWorld() instanceof ServerWorld world) || context.getPlayer() == null) {
            return false;
        }

        int length = params.has("length") ? params.get("length").getAsInt() : 16;
        int width = params.has("width") ? params.get("width").getAsInt() : 3;
        int height = params.has("height") ? params.get("height").getAsInt() : 3;

        float pitch = context.getPlayer().getPitch();
        Direction facing;
        if (pitch < -45.0f) {
            facing = Direction.UP;
        } else if (pitch > 45.0f) {
            facing = Direction.DOWN;
        } else {
            facing = context.getPlayer().getHorizontalFacing();
        }

        Direction right;
        Direction up;
        if (facing == Direction.UP || facing == Direction.DOWN) {
            Direction horiz = context.getPlayer().getHorizontalFacing();
            right = horiz.rotateYClockwise();
            up = facing == Direction.UP ? horiz : horiz.getOpposite();
        } else {
            right = facing.rotateYClockwise();
            up = Direction.UP;
        }

        BlockPos start = context.getPlayer().getBlockPos().offset(facing, 1);
        int halfW = width / 2;
        int mined = 0;

        world.playSound(null, context.getPlayer().getBlockPos(), SoundEvents.ENTITY_WARDEN_DIG, SoundCategory.PLAYERS, 1.5f, 0.8f);

        for (int d = 0; d < length; d++) {
            for (int w = -halfW; w <= halfW; w++) {
                for (int h = 0; h < height; h++) {
                    BlockPos targetPos = start
                            .offset(facing, d)
                            .offset(right, w)
                            .offset(up, h);
                    BlockState state = world.getBlockState(targetPos);
                    if (!state.isAir() && state.getHardness(world, targetPos) >= 0
                            && state.getHardness(world, targetPos) < 50) {
                        world.breakBlock(targetPos, true, context.getPlayer());
                        mined++;
                    }
                }
            }
            BlockPos faceCenter = start.offset(facing, d).offset(up, 1);
            world.spawnParticles(ParticleTypes.CLOUD, faceCenter.getX() + 0.5, faceCenter.getY() + 0.5, faceCenter.getZ() + 0.5, 5, 0.5, 0.5, 0.5, 0.02);
        }

        return mined > 0;
    }
}

