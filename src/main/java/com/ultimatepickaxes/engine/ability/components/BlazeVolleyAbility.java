package com.ultimatepickaxes.engine.ability.components;

import com.google.gson.JsonObject;
import com.ultimatepickaxes.engine.ability.AbilityComponent;
import com.ultimatepickaxes.engine.ability.AbilityContext;
import net.minecraft.entity.projectile.SmallFireballEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;

public class BlazeVolleyAbility implements AbilityComponent {

    @Override
    public boolean execute(JsonObject params, AbilityContext context) {
        if (!(context.getWorld() instanceof ServerWorld world) || context.getPlayer() == null) {
            return false;
        }

        int count = params.has("count") ? params.get("count").getAsInt() : 5;
        Vec3d eyePos = context.getPlayer().getEyePos();
        Vec3d look = context.getPlayer().getRotationVector();

        world.playSound(null, context.getPlayer().getBlockPos(), SoundEvents.ENTITY_BLAZE_SHOOT, SoundCategory.PLAYERS, 1.5f, 1.0f);

        for (int i = 0; i < count; i++) {
            double spreadX = (world.getRandom().nextDouble() - 0.5) * 0.2;
            double spreadY = (world.getRandom().nextDouble() - 0.5) * 0.2;
            double spreadZ = (world.getRandom().nextDouble() - 0.5) * 0.2;
            Vec3d dir = look.add(spreadX, spreadY, spreadZ).normalize();

            SmallFireballEntity fireball = new SmallFireballEntity(world, context.getPlayer(), dir);
            fireball.setPosition(eyePos.x + dir.x * 1.5, eyePos.y + dir.y * 1.5, eyePos.z + dir.z * 1.5);
            world.spawnEntity(fireball);
        }
        return true;
    }
}
