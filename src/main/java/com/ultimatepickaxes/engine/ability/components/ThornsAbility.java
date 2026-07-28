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

public class ThornsAbility implements AbilityComponent {

    @Override
    public boolean execute(JsonObject params, AbilityContext context) {
        if (!(context.getWorld() instanceof ServerWorld world) || context.getPlayer() == null) {
            return false;
        }

        int radius = params.has("radius") ? params.get("radius").getAsInt() : 6;
        float damage = params.has("damage") ? params.get("damage").getAsFloat() : 8.0f;

        BlockPos center = context.getPlayer().getBlockPos();
        world.playSound(null, center, SoundEvents.ENCHANT_THORNS_HIT, SoundCategory.PLAYERS, 2.0f, 1.2f);
        world.spawnParticles(ParticleTypes.CRIT, center.getX() + 0.5, center.getY() + 1, center.getZ() + 0.5, 60, radius / 2.0, 1.0, radius / 2.0, 0.2);

        Box area = new Box(center).expand(radius);
        for (LivingEntity entity : world.getEntitiesByClass(LivingEntity.class, area, e -> e != context.getPlayer())) {
            entity.damage(world.getDamageSources().cactus(), damage);
            entity.setVelocity(entity.getPos().subtract(context.getPlayer().getPos()).normalize().multiply(0.8));
            entity.velocityModified = true;
        }

        return true;
    }
}
