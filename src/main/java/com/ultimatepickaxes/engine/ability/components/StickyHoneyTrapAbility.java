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

public class StickyHoneyTrapAbility implements AbilityComponent {

    @Override
    public boolean execute(JsonObject params, AbilityContext context) {
        if (!(context.getWorld() instanceof ServerWorld world) || context.getPlayer() == null) {
            return false;
        }

        Vec3d center = context.getPlayer().getPos();
        BlockPos playerPos = context.getPlayer().getBlockPos();

        world.playSound(null, playerPos, SoundEvents.BLOCK_HONEY_BLOCK_BREAK, SoundCategory.PLAYERS, 2.5f, 0.8f);
        world.spawnParticles(ParticleTypes.FALLING_HONEY, center.x, center.y + 1, center.z, 100, 3.0, 1.0, 3.0, 0.1);

        for (BlockPos pos : BlockPos.iterate(playerPos.add(-3, -1, -3), playerPos.add(3, -1, 3))) {
            if (world.getBlockState(pos).isSolidBlock(world, pos) && !world.getBlockState(pos).isOf(Blocks.BEDROCK)) {
                world.setBlockState(pos, Blocks.HONEY_BLOCK.getDefaultState());
            }
        }

        Box box = new Box(playerPos).expand(6.0);
        float totalStolenHealth = 0.0f;
        for (LivingEntity entity : world.getEntitiesByClass(LivingEntity.class, box, e -> e != context.getPlayer())) {
            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 240, 255, false, true));
            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE, 240, 4, false, true));
            entity.damage(world.getDamageSources().magic(), 10.0f);
            totalStolenHealth += 4.0f;
        }

        context.getPlayer().clearStatusEffects(); // Cleanse all negative effects!
        context.getPlayer().heal(totalStolenHealth + 6.0f);
        context.getPlayer().addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 400, 1, false, true));
        context.getPlayer().addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 300, 1, false, true));

        return true;
    }
}
