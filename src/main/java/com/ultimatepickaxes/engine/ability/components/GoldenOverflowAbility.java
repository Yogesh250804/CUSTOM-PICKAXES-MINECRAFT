package com.ultimatepickaxes.engine.ability.components;

import com.google.gson.JsonObject;
import com.ultimatepickaxes.engine.ability.AbilityComponent;
import com.ultimatepickaxes.engine.ability.AbilityContext;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public class GoldenOverflowAbility implements AbilityComponent {

    @Override
    public boolean execute(JsonObject params, AbilityContext context) {
        if (!(context.getWorld() instanceof ServerWorld world) || context.getPlayer() == null) {
            return false;
        }

        BlockPos center = context.getPlayer().getBlockPos();
        Vec3d pos = context.getPlayer().getPos();

        // 1. Divine Totem Resurrection Sound & Particles
        world.playSound(null, center, SoundEvents.ITEM_TOTEM_USE, SoundCategory.PLAYERS, 2.5f, 1.0f);
        world.spawnParticles(ParticleTypes.TOTEM_OF_UNDYING, pos.x, pos.y + 1.5, pos.z, 150, 2.5, 2.5, 2.5, 0.2);
        world.spawnParticles(ParticleTypes.FLASH, pos.x, pos.y + 1.5, pos.z, 3, 0.5, 0.5, 0.5, 0.1);

        // 2. Drop 3 Golden Apples on floor around player
        for (int i = 0; i < 3; i++) {
            double rx = (world.random.nextDouble() - 0.5) * 2.0;
            double rz = (world.random.nextDouble() - 0.5) * 2.0;
            ItemEntity goldenAppleItem = new ItemEntity(world, pos.x + rx, pos.y + 1.0, pos.z + rz, new ItemStack(Items.GOLDEN_APPLE));
            world.spawnEntity(goldenAppleItem);
        }

        // 3. Grant Divine Golden Shield (Absorption X gives huge yellow heart shield, Regeneration V, Resistance III)
        context.getPlayer().addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 200, 4, false, true));
        context.getPlayer().addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 1200, 9, false, true));
        context.getPlayer().addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 600, 2, false, true));
        context.getPlayer().addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 600, 0, false, true));
        context.getPlayer().getHungerManager().add(20, 1.0f);

        // 4. Holy Golden Lightning Strike on hostile mobs in 15 blocks
        Box box = new Box(center).expand(15.0);
        for (LivingEntity entity : world.getEntitiesByClass(LivingEntity.class, box, e -> e != context.getPlayer())) {
            LightningEntity lightning = EntityType.LIGHTNING_BOLT.create(world);
            if (lightning != null) {
                lightning.setPosition(entity.getPos());
                lightning.setCosmetic(true); // Divine lightning doesn't burn surroundings
                world.spawnEntity(lightning);
            }
            entity.damage(world.getDamageSources().magic(), 25.0f);
            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 160, 0, false, true));
            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 300, 0, false, true));
            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 160, 3, false, true));
        }

        return true;
    }
}
