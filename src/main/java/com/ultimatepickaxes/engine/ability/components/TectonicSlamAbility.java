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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public class TectonicSlamAbility implements AbilityComponent {

    @Override
    public boolean execute(JsonObject params, AbilityContext context) {
        if (!(context.getWorld() instanceof ServerWorld world) || context.getPlayer() == null) {
            return false;
        }

        int length = params.has("length") ? params.get("length").getAsInt() : 10;
        float damage = params.has("damage") ? params.get("damage").getAsFloat() : 10.0f;

        Vec3d look = context.getPlayer().getRotationVector();
        BlockPos start = context.getPlayer().getBlockPos().offset(context.getPlayer().getHorizontalFacing(), 1);

        world.playSound(null, context.getPlayer().getBlockPos(), SoundEvents.ENTITY_IRON_GOLEM_ATTACK, SoundCategory.PLAYERS, 2.0f, 0.5f);

        BlockStateParticleEffect graniteParticle = new BlockStateParticleEffect(ParticleTypes.BLOCK, Blocks.GRANITE.getDefaultState());

        // Create fissure line
        for (int i = 0; i < length; i++) {
            BlockPos linePos = start.offset(context.getPlayer().getHorizontalFacing(), i);

            // Break ground blocks to create fissure
            BlockPos groundPos = linePos.down();
            if (!world.getBlockState(groundPos).isAir()) {
                world.breakBlock(groundPos, true, context.getPlayer());
            }

            // Raise blocks on sides
            BlockPos leftPos = linePos.offset(context.getPlayer().getHorizontalFacing().rotateYClockwise(), 1);
            BlockPos rightPos = linePos.offset(context.getPlayer().getHorizontalFacing().rotateYCounterclockwise(), 1);

            if (world.getBlockState(leftPos).isAir() && !world.getBlockState(leftPos.down()).isAir()) {
                world.setBlockState(leftPos, Blocks.GRANITE.getDefaultState());
            }
            if (world.getBlockState(rightPos).isAir() && !world.getBlockState(rightPos.down()).isAir()) {
                world.setBlockState(rightPos, Blocks.GRANITE.getDefaultState());
            }

            // Spawn particles
            world.spawnParticles(graniteParticle, linePos.getX() + 0.5, linePos.getY() + 0.5, linePos.getZ() + 0.5, 15, 0.5, 0.5, 0.5, 0.1);

            // Damage and launch mobs in the fissure path
            Box hitBox = new Box(linePos).expand(1.5);
            for (LivingEntity entity : world.getEntitiesByClass(LivingEntity.class, hitBox, e -> e != context.getPlayer())) {
                entity.damage(world.getDamageSources().playerAttack(context.getPlayer()), damage);
                Vec3d launch = new Vec3d(0, 1.5, 0).add(look.multiply(0.5));
                entity.setVelocity(launch);
                entity.velocityModified = true;
                if (entity instanceof ServerPlayerEntity sp) {
                    sp.networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(sp));
                }
            }
        }

        return true;
    }
}
