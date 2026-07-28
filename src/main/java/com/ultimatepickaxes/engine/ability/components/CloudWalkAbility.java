package com.ultimatepickaxes.engine.ability.components;

import com.google.gson.JsonObject;
import com.ultimatepickaxes.engine.ability.AbilityComponent;
import com.ultimatepickaxes.engine.ability.AbilityContext;
import net.minecraft.block.Blocks;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;

public class CloudWalkAbility implements AbilityComponent {

    @Override
    public boolean execute(JsonObject params, AbilityContext context) {
        if (!(context.getWorld() instanceof ServerWorld world) || context.getPlayer() == null) {
            return false;
        }

        int radius = params.has("radius") ? params.get("radius").getAsInt() : 3;
        BlockPos center = context.getPlayer().getBlockPos().down();

        world.playSound(null, center, SoundEvents.BLOCK_WOOL_PLACE, SoundCategory.PLAYERS, 1.5f, 1.2f);
        world.spawnParticles(ParticleTypes.CLOUD, center.getX() + 0.5, center.getY() + 1, center.getZ() + 0.5, 40, radius, 0.5, radius, 0.05);

        for (BlockPos pos : BlockPos.iterate(center.add(-radius, 0, -radius), center.add(radius, 0, radius))) {
            BlockPos immutable = pos.toImmutable();
            if (world.getBlockState(immutable).isAir()) {
                world.setBlockState(immutable, Blocks.WHITE_WOOL.getDefaultState());
            }
        }
        return true;
    }
}
