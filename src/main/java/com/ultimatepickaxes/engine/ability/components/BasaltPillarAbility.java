package com.ultimatepickaxes.engine.ability.components;

import com.google.gson.JsonObject;
import com.ultimatepickaxes.engine.ability.AbilityComponent;
import com.ultimatepickaxes.engine.ability.AbilityContext;
import net.minecraft.block.Blocks;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class BasaltPillarAbility implements AbilityComponent {

    @Override
    public boolean execute(JsonObject params, AbilityContext context) {
        if (!(context.getWorld() instanceof ServerWorld world) || context.getPlayer() == null) {
            return false;
        }

        int height = params.has("height") ? params.get("height").getAsInt() : 8;
        HitResult hit = context.getPlayer().raycast(16.0, 0.0f, false);
        Vec3d targetVec = hit.getPos();
        BlockPos base = BlockPos.ofFloored(targetVec);

        world.playSound(null, base, SoundEvents.BLOCK_BASALT_PLACE, SoundCategory.PLAYERS, 2.0f, 0.6f);

        for (int h = 0; h < height; h++) {
            BlockPos pos = base.up(h);
            if (world.getBlockState(pos).isAir() || world.getBlockState(pos).isReplaceable()) {
                world.setBlockState(pos, Blocks.BASALT.getDefaultState());
            }
        }
        return true;
    }
}
