package com.ultimatepickaxes.engine.ability.components;

import com.google.gson.JsonObject;
import com.ultimatepickaxes.engine.ability.AbilityComponent;
import com.ultimatepickaxes.engine.ability.AbilityContext;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.block.Blocks;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public class RockfallAbility implements AbilityComponent {

    @Override
    public boolean execute(JsonObject params, AbilityContext context) {
        if (!(context.getWorld() instanceof ServerWorld world) || context.getPlayer() == null) {
            return false;
        }

        int count = params.has("count") ? params.get("count").getAsInt() : 12;
        int height = params.has("height") ? params.get("height").getAsInt() : 15;
        int radius = params.has("radius") ? params.get("radius").getAsInt() : 6;
        float damage = params.has("damage") ? params.get("damage").getAsFloat() : 6.0f;

        Vec3d targetVec;
        net.minecraft.util.hit.HitResult hit = context.getPlayer().raycast(20.0, 0.0f, false);
        targetVec = hit.getPos();

        BlockPos target = BlockPos.ofFloored(targetVec);

        world.playSound(null, target, SoundEvents.ENTITY_IRON_GOLEM_ATTACK, SoundCategory.PLAYERS, 2.0f, 0.5f);

        // Spawn falling block entities from above
        for (int i = 0; i < count; i++) {
            double offsetX = (world.getRandom().nextDouble() - 0.5) * radius * 2;
            double offsetZ = (world.getRandom().nextDouble() - 0.5) * radius * 2;

            FallingBlockEntity fallingBlock = FallingBlockEntity.spawnFromBlock(world,
                    new BlockPos(target.getX() + (int) offsetX, target.getY() + height + world.getRandom().nextInt(5), target.getZ() + (int) offsetZ),
                    Blocks.TUFF.getDefaultState());

            if (fallingBlock != null) {
                fallingBlock.setHurtEntities(damage, 40);
                fallingBlock.dropItem = false;
                fallingBlock.timeFalling = 1;
            }
        }

        // Spawn dust particles at target
        world.spawnParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE,
                target.getX() + 0.5, target.getY() + height, target.getZ() + 0.5,
                30, radius / 2.0, 1.0, radius / 2.0, 0.02);

        return true;
    }
}
