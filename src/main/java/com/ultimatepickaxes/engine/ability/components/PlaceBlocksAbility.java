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

public class PlaceBlocksAbility implements AbilityComponent {

    @Override
    public boolean execute(JsonObject params, AbilityContext context) {
        if (!(context.getWorld() instanceof ServerWorld world) || context.getPlayer() == null) {
            return false;
        }

        String blockStr = params.has("blockId") ? params.get("blockId").getAsString() : "minecraft:torch";
        Block block = Registries.BLOCK.get(Identifier.of(blockStr));
        if (block == Blocks.AIR) block = Blocks.TORCH;

        int radius = params.has("radius") ? params.get("radius").getAsInt() : 1;
        BlockPos center = context.getPos() != null ? context.getPos() : context.getPlayer().getBlockPos();

        for (BlockPos pos : BlockPos.iterate(center.add(-radius, -radius, -radius), center.add(radius, radius, radius))) {
            if (world.getBlockState(pos).isAir()) {
                world.setBlockState(pos, block.getDefaultState());
            }
        }
        return true;
    }
}
