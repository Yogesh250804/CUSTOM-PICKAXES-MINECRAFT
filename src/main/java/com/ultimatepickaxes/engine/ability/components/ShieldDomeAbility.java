package com.ultimatepickaxes.engine.ability.components;

import com.google.gson.JsonObject;
import com.ultimatepickaxes.engine.ability.AbilityComponent;
import com.ultimatepickaxes.engine.ability.AbilityContext;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class ShieldDomeAbility implements AbilityComponent {

    @Override
    public boolean execute(JsonObject params, AbilityContext context) {
        if (!(context.getWorld() instanceof ServerWorld world) || context.getPlayer() == null) {
            return false;
        }

        int radius = params.has("radius") ? params.get("radius").getAsInt() : 4;
        String blockStr = params.has("material") ? params.get("material").getAsString() : "minecraft:glass";
        Block block = Registries.BLOCK.get(Identifier.of(blockStr));
        if (block == Blocks.AIR) block = Blocks.GLASS;

        BlockPos center = context.getPlayer().getBlockPos();
        world.playSound(null, center, SoundEvents.BLOCK_GLASS_PLACE, SoundCategory.PLAYERS, 1.5f, 0.8f);

        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    double dist = Math.sqrt(x * x + y * y + z * z);
                    if (dist >= radius - 0.6 && dist <= radius + 0.6) {
                        BlockPos targetPos = center.add(x, y, z);
                        if (world.getBlockState(targetPos).isAir()) {
                            world.setBlockState(targetPos, block.getDefaultState());
                        }
                    }
                }
            }
        }

        world.spawnParticles(ParticleTypes.END_ROD, center.getX() + 0.5, center.getY() + 1, center.getZ() + 0.5, 30, radius / 2.0, radius / 2.0, radius / 2.0, 0.05);
        return true;
    }
}
