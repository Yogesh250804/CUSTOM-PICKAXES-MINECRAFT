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

public class BouncyFortressAbility implements AbilityComponent {

    @Override
    public boolean execute(JsonObject params, AbilityContext context) {
        if (!(context.getWorld() instanceof ServerWorld world) || context.getPlayer() == null) {
            return false;
        }

        int radius = params.has("radius") ? params.get("radius").getAsInt() : 2;
        BlockPos center = context.getPlayer().getBlockPos().down();

        world.playSound(null, center, SoundEvents.BLOCK_SLIME_BLOCK_PLACE, SoundCategory.PLAYERS, 2.0f, 1.0f);
        world.spawnParticles(ParticleTypes.ITEM_SLIME, center.getX() + 0.5, center.getY() + 1, center.getZ() + 0.5, 50, radius, 0.5, radius, 0.1);

        for (BlockPos pos : BlockPos.iterate(center.add(-radius, 0, -radius), center.add(radius, 0, radius))) {
            BlockPos immutable = pos.toImmutable();
            world.setBlockState(immutable, Blocks.SLIME_BLOCK.getDefaultState());
        }

        context.getPlayer().setVelocity(0, 1.8, 0);
        context.getPlayer().velocityModified = true;
        return true;
    }
}
