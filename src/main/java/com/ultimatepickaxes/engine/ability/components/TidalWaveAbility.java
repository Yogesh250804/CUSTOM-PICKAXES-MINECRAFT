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
import net.minecraft.util.math.Vec3d;

public class TidalWaveAbility implements AbilityComponent {

    @Override
    public boolean execute(JsonObject params, AbilityContext context) {
        if (!(context.getWorld() instanceof ServerWorld world) || context.getPlayer() == null) {
            return false;
        }

        int distance = params.has("distance") ? params.get("distance").getAsInt() : 12;
        Vec3d look = context.getPlayer().getRotationVector();
        BlockPos start = context.getPlayer().getBlockPos().offset(context.getPlayer().getHorizontalFacing(), 1);

        world.playSound(null, start, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.PLAYERS, 2.0f, 0.6f);
        context.getPlayer().addStatusEffect(new StatusEffectInstance(StatusEffects.DOLPHINS_GRACE, 400, 0, false, false));
        context.getPlayer().addStatusEffect(new StatusEffectInstance(StatusEffects.WATER_BREATHING, 400, 0, false, false));

        for (int i = 0; i < distance; i++) {
            BlockPos linePos = start.offset(context.getPlayer().getHorizontalFacing(), i);
            world.spawnParticles(ParticleTypes.SPLASH, linePos.getX() + 0.5, linePos.getY() + 1, linePos.getZ() + 0.5, 30, 1.0, 1.0, 1.0, 0.2);

            Box hitBox = new Box(linePos).expand(2.0);
            for (LivingEntity entity : world.getEntitiesByClass(LivingEntity.class, hitBox, e -> e != context.getPlayer())) {
                entity.damage(world.getDamageSources().playerAttack(context.getPlayer()), 8.0f);
                entity.setVelocity(look.multiply(2.0).add(0, 0.5, 0));
                entity.velocityModified = true;
            }
        }
        return true;
    }
}
