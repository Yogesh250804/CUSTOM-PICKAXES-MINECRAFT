package com.ultimatepickaxes.engine.ability.components;

import com.google.gson.JsonObject;
import com.ultimatepickaxes.engine.ability.AbilityComponent;
import com.ultimatepickaxes.engine.ability.AbilityContext;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public class SolarBeamAbility implements AbilityComponent {

    @Override
    public boolean execute(JsonObject params, AbilityContext context) {
        if (!(context.getWorld() instanceof ServerWorld world) || context.getPlayer() == null) {
            return false;
        }

        double range = params.has("range") ? params.get("range").getAsDouble() : 20.0;
        float damage = params.has("damage") ? params.get("damage").getAsFloat() : 12.0f;

        Vec3d eyePos = context.getPlayer().getEyePos();
        Vec3d look = context.getPlayer().getRotationVector();

        world.playSound(null, context.getPlayer().getBlockPos(), SoundEvents.BLOCK_BEACON_POWER_SELECT, SoundCategory.PLAYERS, 2.0f, 1.8f);

        for (int i = 1; i <= (int) range; i++) {
            Vec3d point = eyePos.add(look.multiply(i));
            BlockPos pos = BlockPos.ofFloored(point);

            world.spawnParticles(ParticleTypes.END_ROD, point.x, point.y, point.z, 3, 0.1, 0.1, 0.1, 0.01);
            world.spawnParticles(ParticleTypes.FLAME, point.x, point.y, point.z, 2, 0.1, 0.1, 0.1, 0.01);

            BlockState state = world.getBlockState(pos);
            if (!state.isAir() && state.getHardness(world, pos) >= 0 && state.getHardness(world, pos) < 30) {
                world.breakBlock(pos, true, context.getPlayer());
            }

            Box hitBox = new Box(point.add(-1.0, -1.0, -1.0), point.add(1.0, 1.0, 1.0));
            for (LivingEntity entity : world.getEntitiesByClass(LivingEntity.class, hitBox, e -> e != context.getPlayer())) {
                entity.damage(world.getDamageSources().onFire(), damage);
                entity.setOnFireFor(6);
            }
        }
        return true;
    }
}
