package com.ultimatepickaxes.engine.ability.components;

import com.google.gson.JsonObject;
import com.ultimatepickaxes.engine.ability.AbilityComponent;
import com.ultimatepickaxes.engine.ability.AbilityContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public class OrbitalLaserAbility implements AbilityComponent {

    @Override
    public boolean execute(JsonObject params, AbilityContext context) {
        if (!(context.getWorld() instanceof ServerWorld world) || context.getPlayer() == null) {
            return false;
        }

        float damage = params.has("damage") ? params.get("damage").getAsFloat() : 25.0f;
        HitResult hit = context.getPlayer().raycast(30.0, 0.0f, false);
        Vec3d targetVec = hit.getPos();
        BlockPos target = BlockPos.ofFloored(targetVec);

        world.playSound(null, target, SoundEvents.BLOCK_BEACON_ACTIVATE, SoundCategory.PLAYERS, 2.0f, 1.5f);

        for (int y = 0; y < 30; y++) {
            world.spawnParticles(ParticleTypes.END_ROD, target.getX() + 0.5, target.getY() + y, target.getZ() + 0.5, 10, 0.2, 0.5, 0.2, 0.05);
            world.spawnParticles(ParticleTypes.FLASH, target.getX() + 0.5, target.getY() + y, target.getZ() + 0.5, 2, 0.2, 0.5, 0.2, 0.05);
        }

        Box hitBox = new Box(target).expand(3.0);
        for (LivingEntity entity : world.getEntitiesByClass(LivingEntity.class, hitBox, e -> e != context.getPlayer())) {
            entity.damage(world.getDamageSources().magic(), damage);
            entity.setOnFireFor(10);
        }
        return true;
    }
}
