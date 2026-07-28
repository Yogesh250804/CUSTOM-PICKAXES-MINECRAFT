package com.ultimatepickaxes.engine.ability.components;

import com.google.gson.JsonObject;
import com.ultimatepickaxes.engine.ability.AbilityComponent;
import com.ultimatepickaxes.engine.ability.AbilityContext;
import net.minecraft.block.Blocks;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public class AvalancheAbility implements AbilityComponent {

    @Override
    public boolean execute(JsonObject params, AbilityContext context) {
        if (!(context.getWorld() instanceof ServerWorld world) || context.getPlayer() == null) {
            return false;
        }

        HitResult hit = context.getPlayer().raycast(16.0, 0.0f, false);
        Vec3d target = hit.getPos();

        world.playSound(null, BlockPos.ofFloored(target), SoundEvents.BLOCK_GRAVEL_BREAK, SoundCategory.PLAYERS, 2.0f, 0.6f);

        BlockStateParticleEffect gravelParticle = new BlockStateParticleEffect(ParticleTypes.BLOCK, Blocks.GRAVEL.getDefaultState());
        world.spawnParticles(gravelParticle, target.x, target.y + 4.0, target.z, 60, 2.0, 1.0, 2.0, 0.2);

        // Spawn falling gravel entities overhead
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                BlockPos spawnPos = BlockPos.ofFloored(target.x + x, target.y + 6.0, target.z + z);
                FallingBlockEntity fallingGravel = FallingBlockEntity.spawnFromBlock(world, spawnPos, Blocks.GRAVEL.getDefaultState());
                fallingGravel.setHurtEntities(3.0f, 20);
            }
        }

        Box box = new Box(target.add(-3, -1, -3), target.add(3, 5, 3));
        for (LivingEntity entity : world.getEntitiesByClass(LivingEntity.class, box, e -> e != context.getPlayer())) {
            entity.damage(world.getDamageSources().fallingBlock(context.getPlayer()), 10.0f);
        }
        return true;
    }
}
