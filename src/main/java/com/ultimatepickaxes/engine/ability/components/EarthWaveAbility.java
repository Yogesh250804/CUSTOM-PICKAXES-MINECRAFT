package com.ultimatepickaxes.engine.ability.components;

import com.google.gson.JsonObject;
import com.ultimatepickaxes.engine.ability.AbilityComponent;
import com.ultimatepickaxes.engine.ability.AbilityContext;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public class EarthWaveAbility implements AbilityComponent {

    @Override
    public boolean execute(JsonObject params, AbilityContext context) {
        if (!(context.getWorld() instanceof ServerWorld world) || context.getPlayer() == null) {
            return false;
        }

        HitResult hit = context.getPlayer().raycast(20.0, 0.0f, false);
        BlockPos targetPos;

        if (hit.getType() == HitResult.Type.BLOCK) {
            BlockHitResult blockHit = (BlockHitResult) hit;
            targetPos = blockHit.getBlockPos().offset(blockHit.getSide());
        } else {
            Vec3d eyePos = context.getPlayer().getEyePos();
            Vec3d look = context.getPlayer().getRotationVector();
            targetPos = BlockPos.ofFloored(eyePos.add(look.multiply(5.0)));
        }

        world.playSound(null, targetPos, SoundEvents.BLOCK_GRAVEL_BREAK, SoundCategory.PLAYERS, 1.8f, 0.8f);

        BlockStateParticleEffect dirtParticle = new BlockStateParticleEffect(ParticleTypes.BLOCK, Blocks.DIRT.getDefaultState());
        world.spawnParticles(dirtParticle, targetPos.getX() + 0.5, targetPos.getY() + 0.5, targetPos.getZ() + 0.5, 40, 0.5, 0.5, 0.5, 0.15);

        // Place dirt block at target position
        if (world.getBlockState(targetPos).isAir()) {
            world.setBlockState(targetPos, Blocks.DIRT.getDefaultState());
        }

        // Place dirt blocks around target
        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                BlockPos extra = targetPos.add(dx, 0, dz);
                if (world.getBlockState(extra).isAir()) {
                    world.setBlockState(extra, Blocks.DIRT.getDefaultState());
                }
            }
        }

        // Damage and knockback nearby mobs around target position
        Box box = new Box(targetPos).expand(3.0);
        Vec3d look = context.getPlayer().getRotationVector();
        for (LivingEntity entity : world.getEntitiesByClass(LivingEntity.class, box, e -> e != context.getPlayer())) {
            entity.damage(world.getDamageSources().playerAttack(context.getPlayer()), 8.0f);
            Vec3d launch = look.multiply(1.2).add(0, 0.8, 0);
            entity.setVelocity(launch);
            entity.velocityModified = true;

            if (entity instanceof ServerPlayerEntity serverPlayer) {
                serverPlayer.networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(serverPlayer));
            }
        }

        return true;
    }
}

