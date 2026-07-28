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

public class EarthWaveAbility implements AbilityComponent {

    @Override
    public boolean execute(JsonObject params, AbilityContext context) {
        if (!(context.getWorld() instanceof ServerWorld world) || context.getPlayer() == null) {
            return false;
        }

        Vec3d eyePos = context.getPlayer().getEyePos();
        Vec3d look = context.getPlayer().getRotationVector();
        double distance = params.has("distance") ? params.get("distance").getAsDouble() : 10.0;

        world.playSound(null, context.getPlayer().getBlockPos(), SoundEvents.BLOCK_GRAVEL_BREAK, SoundCategory.PLAYERS, 1.5f, 0.8f);

        BlockStateParticleEffect dirtParticle = new BlockStateParticleEffect(ParticleTypes.BLOCK, Blocks.DIRT.getDefaultState());

        for (int i = 1; i <= (int) distance; i++) {
            Vec3d point = eyePos.add(look.multiply(i));
            BlockPos pos = BlockPos.ofFloored(point);

            world.spawnParticles(dirtParticle, point.x, point.y - 0.5, point.z, 20, 0.4, 0.4, 0.4, 0.1);

            // Replace air directly in front with dirt block or raise ground
            if (world.getBlockState(pos).isAir() && !world.getBlockState(pos.down()).isAir()) {
                world.setBlockState(pos, Blocks.DIRT.getDefaultState());
            }

            Box box = new Box(point.add(-1.5, -1.5, -1.5), point.add(1.5, 1.5, 1.5));
            for (LivingEntity entity : world.getEntitiesByClass(LivingEntity.class, box, e -> e != context.getPlayer())) {
                entity.damage(world.getDamageSources().playerAttack(context.getPlayer()), 8.0f);
                Vec3d launch = look.multiply(1.5).add(0, 1.2, 0);
                entity.setVelocity(launch);
                entity.velocityModified = true;

                if (entity instanceof ServerPlayerEntity serverPlayer) {
                    serverPlayer.networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(serverPlayer));
                }
            }
        }
        return true;
    }
}
