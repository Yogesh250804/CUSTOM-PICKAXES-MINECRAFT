package com.ultimatepickaxes.engine.ability.components;

import com.google.gson.JsonObject;
import com.ultimatepickaxes.engine.ability.AbilityComponent;
import com.ultimatepickaxes.engine.ability.AbilityContext;
import net.minecraft.entity.TntEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ExplosionAbility implements AbilityComponent {

    @Override
    public boolean execute(JsonObject params, AbilityContext context) {
        if (!(context.getWorld() instanceof ServerWorld world) || context.getPlayer() == null) {
            return false;
        }

        float power = params.has("power") ? params.get("power").getAsFloat() : 3.0f;
        boolean breakBlocks = !params.has("breakBlocks") || params.get("breakBlocks").getAsBoolean();
        boolean launchProjectile = params.has("launchProjectile") && params.get("launchProjectile").getAsBoolean();

        if (launchProjectile) {
            TntEntity tnt = new TntEntity(world, context.getPlayer().getX(), context.getPlayer().getEyeY(), context.getPlayer().getZ(), context.getPlayer());
            tnt.setVelocity(context.getPlayer().getRotationVector().multiply(1.5));
            tnt.setFuse(40);
            world.spawnEntity(tnt);
            return true;
        } else {
            BlockPos pos = context.getPos() != null ? context.getPos() : context.getPlayer().getBlockPos();
            World.ExplosionSourceType sourceType = breakBlocks ? World.ExplosionSourceType.TNT : World.ExplosionSourceType.NONE;
            world.createExplosion(context.getPlayer(), pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, power, sourceType);
            return true;
        }
    }
}
