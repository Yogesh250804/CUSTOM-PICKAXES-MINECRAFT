package com.ultimatepickaxes.engine.ability.components;

import com.google.gson.JsonObject;
import com.ultimatepickaxes.engine.ability.AbilityComponent;
import com.ultimatepickaxes.engine.ability.AbilityContext;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

public class MudTrapAbility implements AbilityComponent {

    @Override
    public boolean execute(JsonObject params, AbilityContext context) {
        if (!(context.getWorld() instanceof ServerWorld world) || context.getPlayer() == null) {
            return false;
        }

        int radius = params.has("radius") ? params.get("radius").getAsInt() : 5;
        int duration = params.has("duration") ? params.get("duration").getAsInt() : 100;
        BlockPos center = context.getPlayer().getBlockPos().offset(context.getPlayer().getHorizontalFacing(), 3);

        world.playSound(null, center, SoundEvents.BLOCK_MUD_BREAK, SoundCategory.PLAYERS, 1.5f, 0.6f);

        for (BlockPos pos : BlockPos.iterate(center.add(-radius, -1, -radius), center.add(radius, 0, radius))) {
            BlockPos immutable = pos.toImmutable();
            if (!world.getBlockState(immutable).isAir() && world.getBlockState(immutable.up()).isAir()) {
                world.setBlockState(immutable, Blocks.MUD.getDefaultState());
            }
        }

        BlockStateParticleEffect mudParticle = new BlockStateParticleEffect(ParticleTypes.BLOCK, Blocks.MUD.getDefaultState());
        world.spawnParticles(mudParticle, center.getX(), center.getY() + 1, center.getZ(), 40, radius / 2.0, 0.5, radius / 2.0, 0.1);

        Box area = new Box(center).expand(radius);
        for (LivingEntity entity : world.getEntitiesByClass(LivingEntity.class, area, e -> e != context.getPlayer())) {
            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, duration, 5, false, true));
            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, duration, 2, false, true));
            entity.setVelocity(0, -0.4, 0);
            entity.velocityModified = true;
        }
        return true;
    }
}
