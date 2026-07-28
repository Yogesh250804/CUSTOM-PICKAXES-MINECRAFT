package com.ultimatepickaxes.engine.ability.components;

import com.google.gson.JsonObject;
import com.ultimatepickaxes.engine.ability.AbilityComponent;
import com.ultimatepickaxes.engine.ability.AbilityContext;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;

public class MobSwarmAbility implements AbilityComponent {

    @Override
    public boolean execute(JsonObject params, AbilityContext context) {
        if (!(context.getWorld() instanceof ServerWorld world) || context.getPlayer() == null) {
            return false;
        }

        BlockPos playerPos = context.getPlayer().getBlockPos();
        world.playSound(null, playerPos, SoundEvents.ENTITY_ZOMBIE_VILLAGER_CURE, SoundCategory.PLAYERS, 1.5f, 0.8f);

        int count = params.has("count") ? params.get("count").getAsInt() : 5;

        for (int i = 0; i < count; i++) {
            double angle = (i / (double) count) * Math.PI * 2;
            int dx = (int) (Math.cos(angle) * 3);
            int dz = (int) (Math.sin(angle) * 3);
            BlockPos spawnPos = playerPos.add(dx, 0, dz);

            ZombieEntity zombie = EntityType.ZOMBIE.create(world);
            if (zombie != null) {
                zombie.refreshPositionAndAngles(spawnPos, 0, 0);
                zombie.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
                zombie.equipStack(EquipmentSlot.HEAD, new ItemStack(Items.IRON_HELMET));
                zombie.setCustomName(net.minecraft.text.Text.literal("Undead Ally"));
                zombie.setCustomNameVisible(true);
                world.spawnEntity(zombie);
            }

            world.spawnParticles(ParticleTypes.SOUL_FIRE_FLAME, spawnPos.getX() + 0.5, spawnPos.getY() + 1.0, spawnPos.getZ() + 0.5, 25, 0.4, 0.5, 0.4, 0.05);
        }

        return true;
    }
}
