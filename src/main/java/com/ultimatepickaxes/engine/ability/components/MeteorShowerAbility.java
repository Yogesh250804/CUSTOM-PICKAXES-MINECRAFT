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
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class MeteorShowerAbility implements AbilityComponent {

    @Override
    public boolean execute(JsonObject params, AbilityContext context) {
        if (!(context.getWorld() instanceof ServerWorld world) || context.getPlayer() == null) {
            return false;
        }

        Vec3d eyePos = context.getPlayer().getEyePos();
        Vec3d look = context.getPlayer().getRotationVector();
        double distance = params.has("distance") ? params.get("distance").getAsDouble() : 15.0;
        BlockPos centerPos = BlockPos.ofFloored(eyePos.add(look.multiply(distance)));

        world.playSound(null, centerPos, SoundEvents.ENTITY_DRAGON_FIREBALL_EXPLODE, SoundCategory.PLAYERS, 2.5f, 0.6f);

        int count = 20;
        int radius = 12;

        for (int i = 0; i < count; i++) {
            double rx = (Math.random() - 0.5) * radius * 2;
            double rz = (Math.random() - 0.5) * radius * 2;
            BlockPos meteorTarget = centerPos.add((int) rx, 0, (int) rz);

            // Render meteor trail from sky
            for (int y = 15; y >= 0; y--) {
                double px = meteorTarget.getX() + 0.5 + (y * 0.2);
                double py = meteorTarget.getY() + y;
                double pz = meteorTarget.getZ() + 0.5 + (y * 0.2);

                world.spawnParticles(ParticleTypes.FLAME, px, py, pz, 5, 0.2, 0.2, 0.2, 0.05);
                world.spawnParticles(ParticleTypes.LARGE_SMOKE, px, py, pz, 3, 0.2, 0.2, 0.2, 0.02);
            }

            // Explosion on impact
            world.createExplosion(
                    context.getPlayer(),
                    meteorTarget.getX() + 0.5,
                    meteorTarget.getY() + 0.5,
                    meteorTarget.getZ() + 0.5,
                    4.0f,
                    true,
                    World.ExplosionSourceType.MOB
            );

            if (world.getBlockState(meteorTarget).isAir() || world.getBlockState(meteorTarget).getHardness(world, meteorTarget) < 10) {
                world.setBlockState(meteorTarget, Blocks.MAGMA_BLOCK.getDefaultState());
            }
        }

        return true;
    }
}
