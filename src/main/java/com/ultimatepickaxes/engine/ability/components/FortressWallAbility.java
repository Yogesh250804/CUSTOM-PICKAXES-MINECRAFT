package com.ultimatepickaxes.engine.ability.components;

import com.google.gson.JsonObject;
import com.ultimatepickaxes.engine.ability.AbilityComponent;
import com.ultimatepickaxes.engine.ability.AbilityContext;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registries;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class FortressWallAbility implements AbilityComponent {

    @Override
    public boolean execute(JsonObject params, AbilityContext context) {
        if (!(context.getWorld() instanceof ServerWorld world) || context.getPlayer() == null) {
            return false;
        }

        int width = params.has("width") ? params.get("width").getAsInt() : 5;
        int height = params.has("height") ? params.get("height").getAsInt() : 3;
        String blockStr = params.has("material") ? params.get("material").getAsString() : "minecraft:stone_bricks";
        Block block = Registries.BLOCK.get(Identifier.of(blockStr));
        if (block == Blocks.AIR) block = Blocks.STONE_BRICKS;

        Direction facing = context.getPlayer().getHorizontalFacing();
        Direction right = facing.rotateYClockwise();
        BlockPos start = context.getPlayer().getBlockPos().offset(facing, 2);

        world.playSound(null, start, SoundEvents.BLOCK_STONE_PLACE, SoundCategory.PLAYERS, 1.5f, 0.7f);

        int halfW = width / 2;
        for (int w = -halfW; w <= halfW; w++) {
            for (int h = 0; h < height; h++) {
                BlockPos target = start.offset(right, w).up(h);
                if (world.getBlockState(target).isAir() || world.getBlockState(target).isReplaceable()) {
                    world.setBlockState(target, block.getDefaultState());
                }
            }
        }
        return true;
    }
}
