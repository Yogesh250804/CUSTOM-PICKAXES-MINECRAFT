package com.ultimatepickaxes.engine.ability.components;

import com.google.gson.JsonObject;
import com.ultimatepickaxes.engine.ability.AbilityComponent;
import com.ultimatepickaxes.engine.ability.AbilityContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public class PrismBeamAbility implements AbilityComponent {

    @Override
    public boolean execute(JsonObject params, AbilityContext context) {
        if (!(context.getWorld() instanceof ServerWorld world) || context.getPlayer() == null) {
            return false;
        }

        double range = params.has("range") ? params.get("range").getAsDouble() : 16.0;
        float damage = params.has("damage") ? params.get("damage").getAsFloat() : 8.0f;

        Vec3d eyePos = context.getPlayer().getEyePos();
        Vec3d look = context.getPlayer().getRotationVector();

        world.playSound(null, context.getPlayer().getBlockPos(), SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.PLAYERS, 1.5f, 1.5f);

        // Split beam into 3 directions
        Vec3d[] beams = new Vec3d[]{
            look,
            look.rotateY((float) Math.toRadians(15)),
            look.rotateY((float) Math.toRadians(-15))
        };

        for (Vec3d beamDir : beams) {
            for (int i = 1; i <= (int) range; i++) {
                Vec3d point = eyePos.add(beamDir.multiply(i));
                world.spawnParticles(ParticleTypes.END_ROD, point.x, point.y, point.z, 2, 0.05, 0.05, 0.05, 0.01);

                Box hitBox = new Box(point.add(-0.8, -0.8, -0.8), point.add(0.8, 0.8, 0.8));
                for (LivingEntity entity : world.getEntitiesByClass(LivingEntity.class, hitBox, e -> e != context.getPlayer())) {
                    entity.damage(world.getDamageSources().magic(), damage);
                }
            }
        }
        return true;
    }
}
