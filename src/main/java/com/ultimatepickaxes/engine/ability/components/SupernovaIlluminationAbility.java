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
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public class SupernovaIlluminationAbility implements AbilityComponent {

    @Override
    public boolean execute(JsonObject params, AbilityContext context) {
        if (!(context.getWorld() instanceof ServerWorld world) || context.getPlayer() == null) {
            return false;
        }

        Vec3d center = context.getPlayer().getPos();
        world.playSound(null, context.getPlayer().getBlockPos(), SoundEvents.ITEM_GLOW_INK_SAC_USE, SoundCategory.PLAYERS, 2.5f, 1.2f);
        world.spawnParticles(ParticleTypes.FLASH, center.x, center.y + 2, center.z, 2, 0.5, 0.5, 0.5, 0.1);
        world.spawnParticles(ParticleTypes.GLOW_SQUID_INK, center.x, center.y + 1, center.z, 100, 3.0, 3.0, 3.0, 0.15);
        world.spawnParticles(ParticleTypes.END_ROD, center.x, center.y + 1, center.z, 60, 4.0, 4.0, 4.0, 0.1);

        Box box = new Box(context.getPlayer().getBlockPos()).expand(25.0);
        for (LivingEntity entity : world.getEntitiesByClass(LivingEntity.class, box, e -> e != context.getPlayer())) {
            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 1200, 0, false, true));
            entity.damage(world.getDamageSources().magic(), 18.0f);
        }

        context.getPlayer().addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 2400, 0, false, true));
        context.getPlayer().addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 300, 1, false, true));

        return true;
    }
}
