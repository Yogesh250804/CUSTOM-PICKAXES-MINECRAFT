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

public class SeismicSlamAbility implements AbilityComponent {

    @Override
    public boolean execute(JsonObject params, AbilityContext context) {
        if (!(context.getWorld() instanceof ServerWorld world) || context.getPlayer() == null) {
            return false;
        }

        int radius = params.has("radius") ? params.get("radius").getAsInt() : 8;
        float damage = params.has("damage") ? params.get("damage").getAsFloat() : 20.0f;

        BlockPos center = context.getPlayer().getBlockPos();
        world.playSound(null, center, SoundEvents.ENTITY_GENERIC_EXPLODE.value(), SoundCategory.PLAYERS, 2.0f, 0.5f);
        world.spawnParticles(ParticleTypes.EXPLOSION_EMITTER, center.getX() + 0.5, center.getY() + 0.5, center.getZ() + 0.5, 3, 0.0, 0.0, 0.0, 0.0);

        for (BlockPos pos : BlockPos.iterate(center.add(-radius, -2, -radius), center.add(radius, 0, radius))) {
            BlockPos immutable = pos.toImmutable();
            if (!world.getBlockState(immutable).isAir() && world.getBlockState(immutable).getHardness(world, immutable) >= 0) {
                world.breakBlock(immutable, true, context.getPlayer());
            }
        }

        Box area = new Box(center).expand(radius);
        for (LivingEntity entity : world.getEntitiesByClass(LivingEntity.class, area, e -> e != context.getPlayer())) {
            entity.damage(world.getDamageSources().playerAttack(context.getPlayer()), damage);
            entity.setVelocity(0, 1.5, 0);
            entity.velocityModified = true;
        }
        return true;
    }
}
