package com.ultimatepickaxes.engine.ability.components;

import com.google.gson.JsonObject;
import com.ultimatepickaxes.engine.ability.AbilityComponent;
import com.ultimatepickaxes.engine.ability.AbilityContext;
import net.minecraft.entity.TntEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ExplosionAbility implements AbilityComponent {

    @Override
    public boolean execute(JsonObject params, AbilityContext context) {
        if (!(context.getWorld() instanceof ServerWorld world) || context.getPlayer() == null) {
            return false;
        }

        float power = params.has("power") ? params.get("power").getAsFloat() : 4.0f;
        boolean breakBlocks = !params.has("breakBlocks") || params.get("breakBlocks").getAsBoolean();
        boolean launchProjectile = params.has("launchProjectile") && params.get("launchProjectile").getAsBoolean();

        World.ExplosionSourceType sourceType = breakBlocks ? World.ExplosionSourceType.TNT : World.ExplosionSourceType.NONE;

        if (launchProjectile) {
            TntEntity tnt1 = new TntEntity(world, context.getPlayer().getX(), context.getPlayer().getEyeY(), context.getPlayer().getZ(), context.getPlayer());
            tnt1.setVelocity(context.getPlayer().getRotationVector().multiply(2.0));
            tnt1.setFuse(20);
            world.spawnEntity(tnt1);

            TntEntity tnt2 = new TntEntity(world, context.getPlayer().getX() + 0.2, context.getPlayer().getEyeY(), context.getPlayer().getZ(), context.getPlayer());
            tnt2.setVelocity(context.getPlayer().getRotationVector().rotateY((float) Math.toRadians(15)).multiply(1.8));
            tnt2.setFuse(25);
            world.spawnEntity(tnt2);

            return true;
        } else {
            HitResult hit = context.getPlayer().raycast(12.0, 0.0f, false);
            Vec3d targetPos = hit.getPos();
            world.createExplosion(context.getPlayer(), targetPos.getX(), targetPos.getY(), targetPos.getZ(), power, sourceType);
            return true;
        }
    }
}
