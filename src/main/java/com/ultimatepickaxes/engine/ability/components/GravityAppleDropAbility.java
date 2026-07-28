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

public class GravityAppleDropAbility implements AbilityComponent {

    @Override
    public boolean execute(JsonObject params, AbilityContext context) {
        if (!(context.getWorld() instanceof ServerWorld world) || context.getPlayer() == null) {
            return false;
        }

        Vec3d eyePos = context.getPlayer().getEyePos();
        Vec3d look = context.getPlayer().getRotationVector();
        Vec3d target = eyePos.add(look.multiply(8.0));
        BlockPos targetPos = BlockPos.ofFloored(target);

        world.playSound(null, context.getPlayer().getBlockPos(), SoundEvents.BLOCK_ANVIL_FALL, SoundCategory.PLAYERS, 2.0f, 0.8f);
        world.spawnParticles(ParticleTypes.CRIT, target.x, target.y + 6, target.z, 80, 2.0, 3.0, 2.0, 0.2);
        world.spawnParticles(ParticleTypes.EXPLOSION, target.x, target.y + 1, target.z, 3, 0.5, 0.5, 0.5, 0.1);

        Box box = new Box(targetPos).expand(4.0);
        for (LivingEntity entity : world.getEntitiesByClass(LivingEntity.class, box, e -> e != context.getPlayer())) {
            entity.damage(world.getDamageSources().fallingAnvil(context.getPlayer()), 18.0f);
            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 200, 3, false, true));
            entity.addVelocity(0, -2.0, 0); // Crush downwards
            entity.velocityModified = true;
        }

        context.getPlayer().addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 600, 1, false, true));
        context.getPlayer().getHungerManager().add(10, 0.8f);

        return true;
    }
}
