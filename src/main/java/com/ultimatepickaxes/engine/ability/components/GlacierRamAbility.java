package com.ultimatepickaxes.engine.ability.components;

import com.google.gson.JsonObject;
import com.ultimatepickaxes.engine.ability.AbilityComponent;
import com.ultimatepickaxes.engine.ability.AbilityContext;
import net.minecraft.block.Blocks;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class GlacierRamAbility implements AbilityComponent {

    @Override
    public boolean execute(JsonObject params, AbilityContext context) {
        if (!(context.getWorld() instanceof ServerWorld world) || context.getPlayer() == null) {
            return false;
        }

        float damage = params.has("damage") ? params.get("damage").getAsFloat() : 12.0f;
        Vec3d eyePos = context.getPlayer().getEyePos();
        Vec3d look = context.getPlayer().getRotationVector();

        world.playSound(null, context.getPlayer().getBlockPos(), SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.PLAYERS, 2.0f, 0.5f);

        BlockPos spawnPos = BlockPos.ofFloored(eyePos.add(look.multiply(2)));
        FallingBlockEntity iceBlock = FallingBlockEntity.spawnFromBlock(world, spawnPos, Blocks.PACKED_ICE.getDefaultState());
        if (iceBlock != null) {
            iceBlock.setVelocity(look.multiply(1.8));
            iceBlock.setHurtEntities(damage, 40);
            iceBlock.dropItem = false;
            return true;
        }
        return false;
    }
}
