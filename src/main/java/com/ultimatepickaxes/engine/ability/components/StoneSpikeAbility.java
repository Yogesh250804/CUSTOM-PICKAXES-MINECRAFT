package com.ultimatepickaxes.engine.ability.components;

import com.google.gson.JsonObject;
import com.ultimatepickaxes.engine.ability.AbilityComponent;
import com.ultimatepickaxes.engine.ability.AbilityContext;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

public class StoneSpikeAbility implements AbilityComponent {

    @Override
    public boolean execute(JsonObject params, AbilityContext context) {
        if (!(context.getWorld() instanceof ServerWorld world) || context.getPlayer() == null) {
            return false;
        }

        int spikeCount = params.has("count") ? params.get("count").getAsInt() : 6;
        float damage = params.has("damage") ? params.get("damage").getAsFloat() : 8.0f;
        int radius = params.has("radius") ? params.get("radius").getAsInt() : 5;

        BlockPos center = context.getPlayer().getBlockPos();
        world.playSound(null, center, SoundEvents.BLOCK_STONE_BREAK, SoundCategory.PLAYERS, 2.0f, 0.5f);

        // Create stone spike pillars in a ring around player
        double angleStep = 2 * Math.PI / spikeCount;
        for (int i = 0; i < spikeCount; i++) {
            double angle = angleStep * i;
            int x = (int) Math.round(Math.cos(angle) * radius);
            int z = (int) Math.round(Math.sin(angle) * radius);
            BlockPos spikeBase = center.add(x, 0, z);

            // Find ground level
            while (world.getBlockState(spikeBase).isAir() && spikeBase.getY() > center.getY() - 3) {
                spikeBase = spikeBase.down();
            }
            spikeBase = spikeBase.up();

            // Build spike pillar (2-4 blocks tall)
            int spikeHeight = 2 + world.getRandom().nextInt(3);
            for (int h = 0; h < spikeHeight; h++) {
                BlockPos spikePos = spikeBase.up(h);
                if (world.getBlockState(spikePos).isAir() || world.getBlockState(spikePos).isReplaceable()) {
                    world.setBlockState(spikePos, Blocks.DIORITE.getDefaultState());
                }
            }

            // Spawn particles at spike tip
            BlockPos tip = spikeBase.up(spikeHeight);
            world.spawnParticles(ParticleTypes.CLOUD, tip.getX() + 0.5, tip.getY() + 0.5, tip.getZ() + 0.5, 10, 0.3, 0.3, 0.3, 0.05);

            // Damage and impale mobs near each spike
            Box spikeBox = new Box(spikeBase).expand(1.5, spikeHeight, 1.5);
            for (LivingEntity entity : world.getEntitiesByClass(LivingEntity.class, spikeBox, e -> e != context.getPlayer())) {
                entity.damage(world.getDamageSources().playerAttack(context.getPlayer()), damage);
                entity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 80, 127, false, true));
                entity.setVelocity(0, 0.8, 0);
                entity.velocityModified = true;
            }
        }

        return true;
    }
}
