package com.ultimatepickaxes.engine.ability.components;

import com.google.gson.JsonObject;
import com.ultimatepickaxes.engine.ability.AbilityComponent;
import com.ultimatepickaxes.engine.ability.AbilityContext;
import net.minecraft.inventory.EnderChestInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;

public class VoidStorageAbility implements AbilityComponent {

    @Override
    public boolean execute(JsonObject params, AbilityContext context) {
        if (!(context.getWorld() instanceof ServerWorld world) || context.getPlayer() == null) {
            return false;
        }

        EnderChestInventory ec = context.getPlayer().getEnderChestInventory();
        world.playSound(null, context.getPlayer().getBlockPos(), SoundEvents.BLOCK_ENDER_CHEST_OPEN, SoundCategory.PLAYERS, 1.5f, 1.0f);
        world.spawnParticles(ParticleTypes.PORTAL, context.getPlayer().getX(), context.getPlayer().getY() + 1, context.getPlayer().getZ(), 40, 0.5, 0.5, 0.5, 0.1);

        // Put active held item stack copy in EC if space allows
        ItemStack main = context.getPlayer().getMainHandStack();
        ec.addStack(main.copy());
        return true;
    }
}
