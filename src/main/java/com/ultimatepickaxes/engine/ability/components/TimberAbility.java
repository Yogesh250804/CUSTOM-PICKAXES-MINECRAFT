package com.ultimatepickaxes.engine.ability.components;

import com.google.gson.JsonObject;
import com.ultimatepickaxes.engine.ability.AbilityComponent;
import com.ultimatepickaxes.engine.ability.AbilityContext;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeavesBlock;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

public class TimberAbility implements AbilityComponent {

    @Override
    public boolean execute(JsonObject params, AbilityContext context) {
        if (!(context.getWorld() instanceof ServerWorld world) || context.getPlayer() == null || context.getPos() == null) {
            return false;
        }

        int maxBlocks = params.has("maxBlocks") ? params.get("maxBlocks").getAsInt() : 128;
        BlockPos startPos = context.getPos() != null ? context.getPos() : context.getPlayer().getBlockPos();
        BlockState startState = world.getBlockState(startPos);

        // If clicked position isn't a log, check adjacent blocks or player facing raycast
        if (!startState.isIn(BlockTags.LOGS)) {
            BlockPos foundLog = null;
            for (int dx = -2; dx <= 2; dx++) {
                for (int dy = -2; dy <= 2; dy++) {
                    for (int dz = -2; dz <= 2; dz++) {
                        BlockPos check = startPos.add(dx, dy, dz);
                        if (world.getBlockState(check).isIn(BlockTags.LOGS)) {
                            foundLog = check;
                            break;
                        }
                    }
                    if (foundLog != null) break;
                }
                if (foundLog != null) break;
            }
            if (foundLog != null) {
                startPos = foundLog;
            } else {
                // Fallback: spawn wood wave particles and grant haste/strength buff if no tree nearby
                world.playSound(null, startPos, SoundEvents.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR, SoundCategory.PLAYERS, 1.0f, 1.0f);
                world.spawnParticles(ParticleTypes.EXPLOSION, startPos.getX() + 0.5, startPos.getY() + 1.0, startPos.getZ() + 0.5, 10, 0.5, 0.5, 0.5, 0.1);
                context.getPlayer().addStatusEffect(new net.minecraft.entity.effect.StatusEffectInstance(net.minecraft.entity.effect.StatusEffects.HASTE, 300, 1));
                return true;
            }
        }

        // BFS to find all connected logs and leaves
        Set<BlockPos> visited = new HashSet<>();
        Queue<BlockPos> queue = new ArrayDeque<>();
        Set<BlockPos> logsToBreak = new HashSet<>();
        Set<BlockPos> leavesToBreak = new HashSet<>();

        queue.add(startPos);
        visited.add(startPos);

        while (!queue.isEmpty() && (logsToBreak.size() + leavesToBreak.size()) < maxBlocks) {
            BlockPos current = queue.poll();
            BlockState currentState = world.getBlockState(current);

            if (currentState.isIn(BlockTags.LOGS)) {
                logsToBreak.add(current.toImmutable());
            } else if (currentState.isIn(BlockTags.LEAVES)) {
                leavesToBreak.add(current.toImmutable());
            } else {
                continue;
            }

            // Check all 26 neighbors
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    for (int dz = -1; dz <= 1; dz++) {
                        if (dx == 0 && dy == 0 && dz == 0) continue;
                        BlockPos neighbor = current.add(dx, dy, dz);
                        if (!visited.contains(neighbor)) {
                            visited.add(neighbor);
                            BlockState neighborState = world.getBlockState(neighbor);
                            if (neighborState.isIn(BlockTags.LOGS) || neighborState.isIn(BlockTags.LEAVES)) {
                                queue.add(neighbor);
                            }
                        }
                    }
                }
            }
        }

        // Break all logs first (drops items)
        for (BlockPos logPos : logsToBreak) {
            world.breakBlock(logPos, true, context.getPlayer());
        }

        // Break leaves (drops items like saplings)
        for (BlockPos leafPos : leavesToBreak) {
            world.breakBlock(leafPos, true, context.getPlayer());
        }

        // Spawn particles at the tree location
        world.spawnParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE,
                startPos.getX() + 0.5, startPos.getY() + 3, startPos.getZ() + 0.5,
                30, 1.0, 3.0, 1.0, 0.02);

        return !logsToBreak.isEmpty();
    }
}
