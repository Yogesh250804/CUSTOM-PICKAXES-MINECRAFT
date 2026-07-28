package com.ultimatepickaxes.engine.ability.components;

import com.google.gson.JsonObject;
import com.ultimatepickaxes.engine.ability.AbilityComponent;
import com.ultimatepickaxes.engine.ability.AbilityContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public class ScarecrowAbility implements AbilityComponent {

    @Override
    public boolean execute(JsonObject params, AbilityContext context) {
        if (!(context.getWorld() instanceof ServerWorld world) || context.getPlayer() == null) {
            return false;
        }

        int radius = params.has("radius") ? params.get("radius").getAsInt() : 15;
        BlockPos center = context.getPlayer().getBlockPos();

        world.playSound(null, center, SoundEvents.ENTITY_CREEPER_PRIMED, SoundCategory.PLAYERS, 1.5f, 0.5f);
        world.spawnParticles(ParticleTypes.ANGRY_VILLAGER, center.getX() + 0.5, center.getY() + 1, center.getZ() + 0.5, 40, radius / 2.0, 1.0, radius / 2.0, 0.1);

        Box area = new Box(center).expand(radius);
        for (HostileEntity entity : world.getEntitiesByClass(HostileEntity.class, area, e -> true)) {
            Vec3d diff = entity.getPos().subtract(context.getPlayer().getPos()).normalize().multiply(1.5).add(0, 0.3, 0);
            entity.setVelocity(diff);
            entity.velocityModified = true;
        }
        return true;
    }
}
