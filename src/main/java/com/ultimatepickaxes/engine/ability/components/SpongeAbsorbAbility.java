package com.ultimatepickaxes.engine.ability.components;

import com.google.gson.JsonObject;
import com.ultimatepickaxes.engine.ability.AbilityComponent;
import com.ultimatepickaxes.engine.ability.AbilityContext;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

public class SpongeAbsorbAbility implements AbilityComponent {

    @Override
    public boolean execute(JsonObject params, AbilityContext context) {
        if (!(context.getWorld() instanceof ServerWorld world) || context.getPlayer() == null) {
            return false;
        }

        int radius = params.has("radius") ? params.get("radius").getAsInt() : 8;
        BlockPos center = context.getPlayer().getBlockPos();

        world.playSound(null, center, SoundEvents.BLOCK_SPONGE_ABSORB, SoundCategory.PLAYERS, 2.0f, 1.0f);
        world.spawnParticles(ParticleTypes.SPLASH, center.getX() + 0.5, center.getY() + 1, center.getZ() + 0.5, 80, radius / 2.0, 1.0, radius / 2.0, 0.2);

        int absorbed = 0;
        for (BlockPos pos : BlockPos.iterate(center.add(-radius, -radius, -radius), center.add(radius, radius, radius))) {
            BlockPos immutable = pos.toImmutable();
            if (world.getBlockState(immutable).isOf(Blocks.WATER) || world.getBlockState(immutable).isOf(Blocks.LAVA)) {
                world.setBlockState(immutable, Blocks.AIR.getDefaultState());
                absorbed++;
            }
        }

        // Extinguish fire on player
        context.getPlayer().extinguish();

        // Damage aquatic/fire entities
        Box area = new Box(center).expand(radius);
        for (LivingEntity entity : world.getEntitiesByClass(LivingEntity.class, area, e -> e != context.getPlayer())) {
            if (entity.isWet() || entity.isOnFire()) {
                entity.damage(world.getDamageSources().magic(), 10.0f);
            }
        }

        return absorbed > 0;
    }
}
