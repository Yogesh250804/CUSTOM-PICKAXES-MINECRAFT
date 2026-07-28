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

public class SoulDrainAbility implements AbilityComponent {

    @Override
    public boolean execute(JsonObject params, AbilityContext context) {
        if (!(context.getWorld() instanceof ServerWorld world) || context.getPlayer() == null) {
            return false;
        }

        int radius = params.has("radius") ? params.get("radius").getAsInt() : 6;
        float damage = params.has("damage") ? params.get("damage").getAsFloat() : 8.0f;
        float healAmount = params.has("heal") ? params.get("heal").getAsFloat() : 4.0f;

        BlockPos center = context.getPlayer().getBlockPos();
        world.playSound(null, center, SoundEvents.ENTITY_WITHER_AMBIENT, SoundCategory.PLAYERS, 1.5f, 0.7f);

        Box area = new Box(center).expand(radius);
        int hit = 0;
        for (LivingEntity entity : world.getEntitiesByClass(LivingEntity.class, area, e -> e != context.getPlayer())) {
            entity.damage(world.getDamageSources().wither(), damage);
            world.spawnParticles(ParticleTypes.SOUL, entity.getX(), entity.getY() + 1, entity.getZ(), 15, 0.3, 0.5, 0.3, 0.05);
            hit++;
        }

        if (hit > 0) {
            context.getPlayer().heal(healAmount * hit);
            world.spawnParticles(ParticleTypes.HEART, context.getPlayer().getX(), context.getPlayer().getY() + 1, context.getPlayer().getZ(), 5 * hit, 0.3, 0.5, 0.3, 0.05);
        }
        return true;
    }
}
