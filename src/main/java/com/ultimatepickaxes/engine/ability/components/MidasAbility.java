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

public class MidasAbility implements AbilityComponent {

    @Override
    public boolean execute(JsonObject params, AbilityContext context) {
        if (!(context.getWorld() instanceof ServerWorld world) || context.getPlayer() == null) {
            return false;
        }

        int radius = params.has("radius") ? params.get("radius").getAsInt() : 2;
        BlockPos center = context.getPos() != null ? context.getPos() : context.getPlayer().getBlockPos();

        world.playSound(null, center, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 2.0f, 1.2f);
        world.spawnParticles(ParticleTypes.TOTEM_OF_UNDYING, center.getX() + 0.5, center.getY() + 0.5, center.getZ() + 0.5, 50, radius, radius, radius, 0.1);

        int converted = 0;
        for (BlockPos pos : BlockPos.iterate(center.add(-radius, -radius, -radius), center.add(radius, radius, radius))) {
            BlockPos immutable = pos.toImmutable();
            if (!world.getBlockState(immutable).isAir() && world.getBlockState(immutable).getHardness(world, immutable) >= 0 && !world.getBlockState(immutable).isOf(Blocks.BEDROCK)) {
                world.setBlockState(immutable, Blocks.GOLD_BLOCK.getDefaultState());
                converted++;
            }
        }
        return converted > 0;
    }
}
