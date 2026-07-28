package com.ultimatepickaxes.engine.ability.components;

import com.google.gson.JsonObject;
import com.ultimatepickaxes.engine.ability.AbilityComponent;
import com.ultimatepickaxes.engine.ability.AbilityContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public class PumpkinCurseAbility implements AbilityComponent {

    @Override
    public boolean execute(JsonObject params, AbilityContext context) {
        if (!(context.getWorld() instanceof ServerWorld world) || context.getPlayer() == null) {
            return false;
        }

        Vec3d center = context.getPlayer().getPos();
        world.playSound(null, context.getPlayer().getBlockPos(), SoundEvents.ENTITY_WITCH_CELEBRATE, SoundCategory.PLAYERS, 2.0f, 0.9f);
        world.spawnParticles(ParticleTypes.LAVA, center.x, center.y + 1, center.z, 50, 3.0, 1.0, 3.0, 0.1);
        world.spawnParticles(ParticleTypes.FLAME, center.x, center.y + 2, center.z, 80, 3.0, 2.0, 3.0, 0.1);

        Box box = new Box(context.getPlayer().getBlockPos()).expand(15.0);
        for (LivingEntity entity : world.getEntitiesByClass(LivingEntity.class, box, e -> e != context.getPlayer())) {
            entity.equipStack(EquipmentSlot.HEAD, new ItemStack(Items.CARVED_PUMPKIN));
            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 200, 0, false, true));
            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 300, 0, false, true));
            entity.setOnFireFor(6);
            entity.damage(world.getDamageSources().magic(), 14.0f);
        }

        context.getPlayer().addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 600, 0, false, true));
        context.getPlayer().addStatusEffect(new StatusEffectInstance(StatusEffects.SATURATION, 300, 0, false, true));

        return true;
    }
}
