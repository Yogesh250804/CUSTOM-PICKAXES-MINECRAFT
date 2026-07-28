package com.ultimatepickaxes.engine.ability.components;

import com.google.gson.JsonObject;
import com.ultimatepickaxes.engine.ability.AbilityComponent;
import com.ultimatepickaxes.engine.ability.AbilityContext;
import net.minecraft.block.Blocks;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class StalactiteRainAbility implements AbilityComponent {

    @Override
    public boolean execute(JsonObject params, AbilityContext context) {
        if (!(context.getWorld() instanceof ServerWorld world) || context.getPlayer() == null) {
            return false;
        }

        int count = params.has("count") ? params.get("count").getAsInt() : 15;
        int height = params.has("height") ? params.get("height").getAsInt() : 12;
        int radius = params.has("radius") ? params.get("radius").getAsInt() : 6;
        float damage = params.has("damage") ? params.get("damage").getAsFloat() : 8.0f;

        HitResult hit = context.getPlayer().raycast(20.0, 0.0f, false);
        Vec3d targetVec = hit.getPos();
        BlockPos target = BlockPos.ofFloored(targetVec);

        world.playSound(null, target, SoundEvents.BLOCK_POINTED_DRIPSTONE_FALL, SoundCategory.PLAYERS, 2.0f, 0.5f);

        for (int i = 0; i < count; i++) {
            double offsetX = (world.getRandom().nextDouble() - 0.5) * radius * 2;
            double offsetZ = (world.getRandom().nextDouble() - 0.5) * radius * 2;

            BlockPos spawnPos = new BlockPos(target.getX() + (int) offsetX, target.getY() + height + world.getRandom().nextInt(4), target.getZ() + (int) offsetZ);
            FallingBlockEntity fallingBlock = FallingBlockEntity.spawnFromBlock(world, spawnPos, Blocks.POINTED_DRIPSTONE.getDefaultState());

            if (fallingBlock != null) {
                fallingBlock.setHurtEntities(damage, 40);
                fallingBlock.dropItem = false;
                fallingBlock.timeFalling = 1;
            }
        }
        return true;
    }
}
