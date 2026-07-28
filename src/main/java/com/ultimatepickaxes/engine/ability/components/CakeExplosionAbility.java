package com.ultimatepickaxes.engine.ability.components;

import com.google.gson.JsonObject;
import com.ultimatepickaxes.engine.ability.AbilityComponent;
import com.ultimatepickaxes.engine.ability.AbilityContext;
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

public class CakeExplosionAbility implements AbilityComponent {

    @Override
    public boolean execute(JsonObject params, AbilityContext context) {
        if (!(context.getWorld() instanceof ServerWorld world) || context.getPlayer() == null) {
            return false;
        }

        Vec3d eyePos = context.getPlayer().getEyePos();
        Vec3d look = context.getPlayer().getRotationVector();
        Vec3d target = eyePos.add(look.multiply(8.0));

        world.playSound(null, context.getPlayer().getBlockPos(), SoundEvents.ENTITY_GENERIC_EXPLODE.value(), SoundCategory.PLAYERS, 2.0f, 1.2f);
        world.playSound(null, context.getPlayer().getBlockPos(), SoundEvents.ENTITY_FIREWORK_ROCKET_BLAST, SoundCategory.PLAYERS, 2.0f, 1.0f);

        world.spawnParticles(ParticleTypes.FIREWORK, target.x, target.y + 1, target.z, 80, 2.0, 2.0, 2.0, 0.15);
        world.spawnParticles(ParticleTypes.HEART, target.x, target.y + 1, target.z, 40, 1.5, 1.5, 1.5, 0.1);
        world.spawnParticles(ParticleTypes.HAPPY_VILLAGER, target.x, target.y + 1, target.z, 60, 2.0, 2.0, 2.0, 0.1);

        // Player super sugar boost
        context.getPlayer().addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 400, 3, false, true));
        context.getPlayer().addStatusEffect(new StatusEffectInstance(StatusEffects.HASTE, 400, 2, false, true));
        context.getPlayer().addStatusEffect(new StatusEffectInstance(StatusEffects.JUMP_BOOST, 400, 2, false, true));
        context.getPlayer().getHungerManager().add(20, 1.0f);

        // Encase hit enemies in sticky cake frosting
        Box box = new Box(BlockPos.ofFloored(target)).expand(6.0);
        for (LivingEntity entity : world.getEntitiesByClass(LivingEntity.class, box, e -> e != context.getPlayer())) {
            entity.damage(world.getDamageSources().magic(), 16.0f);
            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 160, 255, false, true));
            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE, 160, 3, false, true));
        }

        return true;
    }
}
