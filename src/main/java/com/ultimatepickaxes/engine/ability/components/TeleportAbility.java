package com.ultimatepickaxes.engine.ability.components;

import com.google.gson.JsonObject;
import com.ultimatepickaxes.engine.ability.AbilityComponent;
import com.ultimatepickaxes.engine.ability.AbilityContext;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.Random;

public class TeleportAbility implements AbilityComponent {
    private final Random random = new Random();

    @Override
    public boolean execute(JsonObject params, AbilityContext context) {
        if (!(context.getWorld() instanceof ServerWorld world) || context.getPlayer() == null) {
            return false;
        }

        double distance = params.has("distance") ? params.get("distance").getAsDouble() : 12.0;
        boolean isRandom = params.has("random") && params.get("random").getAsBoolean();

        Vec3d origin = context.getPlayer().getPos();
        Vec3d target;

        if (isRandom) {
            double rx = (random.nextDouble() - 0.5) * distance * 2;
            double rz = (random.nextDouble() - 0.5) * distance * 2;
            target = origin.add(rx, 0, rz);
        } else {
            HitResult hit = context.getPlayer().raycast(distance, 0.0f, false);
            if (hit.getType() == HitResult.Type.BLOCK) {
                target = hit.getPos();
            } else {
                target = origin.add(context.getPlayer().getRotationVector().multiply(distance));
            }
        }

        BlockPos targetPos = BlockPos.ofFloored(target);
        if (world.getBlockState(targetPos).isSolidBlock(world, targetPos)) {
            targetPos = targetPos.up();
            target = new Vec3d(targetPos.getX() + 0.5, targetPos.getY() + 0.1, targetPos.getZ() + 0.5);
        }

        world.spawnParticles(ParticleTypes.PORTAL, origin.x, origin.y + 1.0, origin.z, 30, 0.5, 0.5, 0.5, 0.2);
        world.playSound(null, context.getPlayer().getBlockPos(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1.0f, 1.0f);

        context.getPlayer().requestTeleport(target.x, target.y, target.z);

        world.spawnParticles(ParticleTypes.DRAGON_BREATH, target.x, target.y + 1.0, target.z, 30, 0.5, 0.5, 0.5, 0.1);
        world.playSound(null, BlockPos.ofFloored(target), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1.0f, 1.0f);
        return true;
    }
}
