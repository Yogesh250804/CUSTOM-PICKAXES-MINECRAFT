package com.ultimatepickaxes.engine.ability.components;

import com.google.gson.JsonObject;
import com.ultimatepickaxes.engine.ability.AbilityComponent;
import com.ultimatepickaxes.engine.ability.AbilityContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
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

        double distance = params.has("distance") ? params.get("distance").getAsDouble() : 8.0;
        boolean isRandom = params.has("random") && params.get("random").getAsBoolean();

        Vec3d target;
        if (isRandom) {
            double rx = (random.nextDouble() - 0.5) * distance * 2;
            double rz = (random.nextDouble() - 0.5) * distance * 2;
            target = context.getPlayer().getPos().add(rx, 0, rz);
        } else {
            Vec3d look = context.getPlayer().getRotationVector();
            target = context.getPlayer().getPos().add(look.multiply(distance));
        }

        BlockPos targetPos = BlockPos.ofFloored(target);
        if (!world.getBlockState(targetPos).isSolidBlock(world, targetPos)) {
            world.playSound(null, context.getPlayer().getBlockPos(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1.0f, 1.0f);
            context.getPlayer().teleport(target.x, target.y, target.z, true);
            world.playSound(null, BlockPos.ofFloored(target), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1.0f, 1.0f);
            return true;
        }
        return false;
    }
}
