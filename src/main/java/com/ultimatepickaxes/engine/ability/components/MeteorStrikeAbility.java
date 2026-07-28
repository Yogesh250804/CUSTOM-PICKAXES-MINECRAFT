package com.ultimatepickaxes.engine.ability.components;

import com.google.gson.JsonObject;
import com.ultimatepickaxes.engine.ability.AbilityComponent;
import com.ultimatepickaxes.engine.ability.AbilityContext;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class MeteorStrikeAbility implements AbilityComponent {

    @Override
    public boolean execute(JsonObject params, AbilityContext context) {
        if (!(context.getWorld() instanceof ServerWorld world) || context.getPlayer() == null) {
            return false;
        }

        int count = params.has("count") ? params.get("count").getAsInt() : 3;
        HitResult hit = context.getPlayer().raycast(25.0, 0.0f, false);
        Vec3d target = hit.getPos();

        world.playSound(null, BlockPos.ofFloored(target), SoundEvents.ENTITY_GHAST_SHOOT, SoundCategory.PLAYERS, 2.0f, 0.5f);

        for (int i = 0; i < count; i++) {
            double rx = (world.getRandom().nextDouble() - 0.5) * 6;
            double rz = (world.getRandom().nextDouble() - 0.5) * 6;
            Vec3d spawnPos = new Vec3d(target.x + rx, target.y + 20 + (i * 3), target.z + rz);
            Vec3d velocity = new Vec3d(0, -1.5, 0);

            FireballEntity fireball = new FireballEntity(world, context.getPlayer(), velocity, 4);
            fireball.setPosition(spawnPos.x, spawnPos.y, spawnPos.z);
            world.spawnEntity(fireball);
        }
        return true;
    }
}
