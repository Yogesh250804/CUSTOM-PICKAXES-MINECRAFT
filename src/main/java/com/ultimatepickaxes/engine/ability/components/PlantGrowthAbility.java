package com.ultimatepickaxes.engine.ability.components;

import com.google.gson.JsonObject;
import com.ultimatepickaxes.engine.ability.AbilityComponent;
import com.ultimatepickaxes.engine.ability.AbilityContext;
import net.minecraft.block.BlockState;
import net.minecraft.block.Fertilizable;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public class PlantGrowthAbility implements AbilityComponent {

    @Override
    public boolean execute(JsonObject params, AbilityContext context) {
        if (!(context.getWorld() instanceof ServerWorld world) || context.getPos() == null) {
            return false;
        }

        int radius = params.has("radius") ? params.get("radius").getAsInt() : 3;
        BlockPos center = context.getPos();
        boolean grewAny = false;

        for (BlockPos pos : BlockPos.iterate(center.add(-radius, -1, -radius), center.add(radius, 1, radius))) {
            BlockState state = world.getBlockState(pos);
            if (state.getBlock() instanceof Fertilizable fertilizable) {
                if (fertilizable.isFertilizable(world, pos, state)) {
                    fertilizable.grow(world, world.random, pos, state);
                    grewAny = true;
                }
            }
        }
        return grewAny;
    }
}
