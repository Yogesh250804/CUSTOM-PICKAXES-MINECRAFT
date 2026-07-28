package com.ultimatepickaxes.engine.ability.components;

import com.google.gson.JsonObject;
import com.ultimatepickaxes.engine.ability.AbilityComponent;
import com.ultimatepickaxes.engine.ability.AbilityContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.List;
import java.util.Random;

public class DimensionalWarpAbility implements AbilityComponent {
    private final Random random = new Random();

    @Override
    public boolean execute(JsonObject params, AbilityContext context) {
        if (!(context.getWorld() instanceof ServerWorld world) || context.getPlayer() == null) {
            return false;
        }

        Vec3d origin = context.getPlayer().getPos();
        world.playSound(null, context.getPlayer().getBlockPos(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 2.0f, 0.8f);
        world.spawnParticles(ParticleTypes.DRAGON_BREATH, origin.x, origin.y + 1, origin.z, 60, 1.0, 1.0, 1.0, 0.1);

        Box searchBox = new Box(context.getPlayer().getBlockPos()).expand(15.0);
        List<LivingEntity> targets = world.getEntitiesByClass(LivingEntity.class, searchBox, e -> e != context.getPlayer());

        for (LivingEntity entity : targets) {
            // Pull mobs into void vortex towards player
            Vec3d dir = origin.subtract(entity.getPos()).normalize().multiply(1.5);
            entity.addVelocity(dir.x, 0.5, dir.z);
            entity.velocityModified = true;
            entity.damage(world.getDamageSources().dragonBreath(), 14.0f);
            world.spawnParticles(ParticleTypes.PORTAL, entity.getX(), entity.getY() + 1, entity.getZ(), 20, 0.5, 0.5, 0.5, 0.1);
        }

        // Random safe teleport for player
        double rx = (random.nextDouble() - 0.5) * 24.0;
        double rz = (random.nextDouble() - 0.5) * 24.0;
        Vec3d targetPos = origin.add(rx, 1.0, rz);
        BlockPos targetBlock = BlockPos.ofFloored(targetPos);

        if (world.getBlockState(targetBlock).isSolidBlock(world, targetBlock)) {
            targetBlock = targetBlock.up(2);
            targetPos = new Vec3d(targetBlock.getX() + 0.5, targetBlock.getY(), targetBlock.getZ() + 0.5);
        }

        context.getPlayer().requestTeleport(targetPos.x, targetPos.y, targetPos.z);
        world.playSound(null, BlockPos.ofFloored(targetPos), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 2.0f, 1.2f);
        world.spawnParticles(ParticleTypes.REVERSE_PORTAL, targetPos.x, targetPos.y + 1, targetPos.z, 50, 0.5, 0.5, 0.5, 0.1);

        return true;
    }
}
