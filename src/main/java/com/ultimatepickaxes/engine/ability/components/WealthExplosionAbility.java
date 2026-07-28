package com.ultimatepickaxes.engine.ability.components;

import com.google.gson.JsonObject;
import com.ultimatepickaxes.engine.ability.AbilityComponent;
import com.ultimatepickaxes.engine.ability.AbilityContext;
import net.minecraft.block.Block;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;

public class WealthExplosionAbility implements AbilityComponent {

    @Override
    public boolean execute(JsonObject params, AbilityContext context) {
        if (!(context.getWorld() instanceof ServerWorld world) || context.getPlayer() == null) {
            return false;
        }

        BlockPos pos = context.getPlayer().getBlockPos();
        world.playSound(null, pos, SoundEvents.ENTITY_VILLAGER_YES, SoundCategory.PLAYERS, 2.0f, 1.0f);
        world.spawnParticles(ParticleTypes.HAPPY_VILLAGER, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, 50, 1.0, 1.0, 1.0, 0.1);

        context.getPlayer().addStatusEffect(new StatusEffectInstance(StatusEffects.HERO_OF_THE_VILLAGE, 600, 2, false, true));

        Block.dropStack(world, pos, new ItemStack(Items.EMERALD, 5 + world.getRandom().nextInt(10)));
        Block.dropStack(world, pos, new ItemStack(Items.GOLD_INGOT, 3 + world.getRandom().nextInt(5)));

        return true;
    }
}
