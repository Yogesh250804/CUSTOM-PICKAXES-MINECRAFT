package com.ultimatepickaxes.engine.ability.components;

import com.google.gson.JsonObject;
import com.ultimatepickaxes.engine.ability.AbilityComponent;
import com.ultimatepickaxes.engine.ability.AbilityContext;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registries;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class BridgeAbility implements AbilityComponent {

    @Override
    public boolean execute(JsonObject params, AbilityContext context) {
        if (!(context.getWorld() instanceof ServerWorld world) || context.getPlayer() == null) {
            return false;
        }

        String blockStr = params.has("material") ? params.get("material").getAsString() : "minecraft:glass";
        Block block = Registries.BLOCK.get(Identifier.of(blockStr));
        if (block == Blocks.AIR) block = Blocks.GLASS;

        int length = params.has("length") ? params.get("length").getAsInt() : 5;
        int width = params.has("width") ? params.get("width").getAsInt() : 3;

        BlockPos playerPos = context.getPlayer().getBlockPos().down();
        var facing = context.getPlayer().getHorizontalFacing();

        int halfW = width / 2;
        boolean placedAny = false;

        for (int l = 0; l < length; l++) {
            BlockPos step = playerPos.offset(facing, l);
            for (int w = -halfW; w <= halfW; w++) {
                BlockPos target = step.offset(facing.rotateYClockwise(), w);
                if (world.getBlockState(target).isAir()) {
                    world.setBlockState(target, block.getDefaultState());
                    placedAny = true;
                }
            }
        }
        return placedAny;
    }
}
