package com.ultimatepickaxes.engine.ability.components;

import com.google.gson.JsonObject;
import com.ultimatepickaxes.engine.ability.AbilityComponent;
import com.ultimatepickaxes.engine.ability.AbilityContext;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class LightningVolleyAbility implements AbilityComponent {

    @Override
    public boolean execute(JsonObject params, AbilityContext context) {
        if (!(context.getWorld() instanceof ServerWorld world) || context.getPlayer() == null) {
            return false;
        }

        Vec3d eyePos = context.getPlayer().getEyePos();
        Vec3d look = context.getPlayer().getRotationVector();
        double distance = params.has("distance") ? params.get("distance").getAsDouble() : 12.0;
        BlockPos centerPos = BlockPos.ofFloored(eyePos.add(look.multiply(distance)));

        world.playSound(null, centerPos, SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER, SoundCategory.PLAYERS, 2.0f, 1.0f);

        int count = params.has("count") ? params.get("count").getAsInt() : 8;
        int radius = 5;

        for (int i = 0; i < count; i++) {
            double angle = (i / (double) count) * Math.PI * 2;
            int rx = (int) (Math.cos(angle) * radius);
            int rz = (int) (Math.sin(angle) * radius);
            BlockPos strikePos = centerPos.add(rx, 0, rz);

            LightningEntity lightning = EntityType.LIGHTNING_BOLT.create(world);
            if (lightning != null) {
                lightning.refreshPositionAfterTeleport(Vec3d.ofBottomCenter(strikePos));
                world.spawnEntity(lightning);
            }

            world.spawnParticles(ParticleTypes.ELECTRIC_SPARK, strikePos.getX() + 0.5, strikePos.getY() + 1.0, strikePos.getZ() + 0.5, 30, 0.5, 0.5, 0.5, 0.2);
        }

        return true;
    }
}
