package com.ultimatepickaxes.engine.ability.components;

import com.google.gson.JsonObject;
import com.ultimatepickaxes.engine.ability.AbilityComponent;
import com.ultimatepickaxes.engine.ability.AbilityContext;
import net.minecraft.block.Blocks;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public class AnvilRainAbility implements AbilityComponent {

    @Override
    public boolean execute(JsonObject params, AbilityContext context) {
        if (!(context.getWorld() instanceof ServerWorld world) || context.getPlayer() == null) {
            return false;
        }

        Vec3d eyePos = context.getPlayer().getEyePos();
        Vec3d look = context.getPlayer().getRotationVector();
        double distance = params.has("distance") ? params.get("distance").getAsDouble() : 10.0;
        BlockPos centerPos = BlockPos.ofFloored(eyePos.add(look.multiply(distance)));

        world.playSound(null, context.getPlayer().getBlockPos(), SoundEvents.BLOCK_ANVIL_USE, SoundCategory.PLAYERS, 2.0f, 0.8f);

        int radius = 3;
        int heightOffset = 12;

        for (int dx = -radius; dx <= radius; dx += 2) {
            for (int dz = -radius; dz <= radius; dz += 2) {
                BlockPos spawnPos = centerPos.add(dx, heightOffset, dz);
                FallingBlockEntity fallingBlock = FallingBlockEntity.spawnFromBlock(
                        world, spawnPos, Blocks.ANVIL.getDefaultState()
                );
                fallingBlock.setHurtEntities(2.0f, 40);
                world.spawnParticles(ParticleTypes.CRIT, spawnPos.getX() + 0.5, spawnPos.getY(), spawnPos.getZ() + 0.5, 10, 0.3, 0.3, 0.3, 0.1);
            }
        }

        // Crushing damage to mobs under rain
        Box box = new Box(centerPos.add(-4, 0, -4).toCenterPos(), centerPos.add(4, 15, 4).toCenterPos());
        for (LivingEntity entity : world.getEntitiesByClass(LivingEntity.class, box, e -> e != context.getPlayer())) {
            entity.damage(world.getDamageSources().playerAttack(context.getPlayer()), 15.0f);
        }

        return true;
    }
}
