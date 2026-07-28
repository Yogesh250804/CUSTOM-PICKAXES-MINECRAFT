package com.ultimatepickaxes.engine.ability.components;

import com.google.gson.JsonObject;
import com.ultimatepickaxes.engine.ability.AbilityComponent;
import com.ultimatepickaxes.engine.ability.AbilityContext;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LadderBlock;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class DeepDrillAbility implements AbilityComponent {

    @Override
    public boolean execute(JsonObject params, AbilityContext context) {
        if (!(context.getWorld() instanceof ServerWorld world) || context.getPlayer() == null) {
            return false;
        }

        int depth = params.has("depth") ? params.get("depth").getAsInt() : 20;
        BlockPos start = context.getPlayer().getBlockPos();

        world.playSound(null, start, SoundEvents.ENTITY_WARDEN_DIG, SoundCategory.PLAYERS, 2.0f, 1.2f);

        int drilled = 0;
        for (int y = 0; y < depth; y++) {
            BlockPos drillPos = start.down(y + 1);

            // Don't drill into void
            if (drillPos.getY() < world.getBottomY()) break;

            BlockState state = world.getBlockState(drillPos);

            // Don't drill through bedrock
            if (state.isOf(Blocks.BEDROCK)) break;

            if (!state.isAir() && state.getHardness(world, drillPos) >= 0) {
                world.breakBlock(drillPos, true, context.getPlayer());
                drilled++;
            }

            // Place ladder on north side for climbing back up
            BlockPos ladderPos = drillPos;
            BlockPos wallPos = ladderPos.north();
            if (!world.getBlockState(wallPos).isAir()) {
                if (world.getBlockState(ladderPos).isAir()) {
                    world.setBlockState(ladderPos, Blocks.LADDER.getDefaultState()
                            .with(LadderBlock.FACING, Direction.SOUTH));
                }
            }

            // Spawn drilling particles
            world.spawnParticles(ParticleTypes.CLOUD,
                    drillPos.getX() + 0.5, drillPos.getY() + 0.5, drillPos.getZ() + 0.5,
                    3, 0.3, 0.1, 0.3, 0.02);
        }

        // Place a torch at the bottom
        BlockPos bottomPos = start.down(drilled);
        if (world.getBlockState(bottomPos).isAir()) {
            world.setBlockState(bottomPos, Blocks.TORCH.getDefaultState());
        }

        return drilled > 0;
    }
}
