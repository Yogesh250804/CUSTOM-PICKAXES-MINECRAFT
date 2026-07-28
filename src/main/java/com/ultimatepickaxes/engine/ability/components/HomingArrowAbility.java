package com.ultimatepickaxes.engine.ability.components;

import com.google.gson.JsonObject;
import com.ultimatepickaxes.engine.ability.AbilityComponent;
import com.ultimatepickaxes.engine.ability.AbilityContext;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;

public class HomingArrowAbility implements AbilityComponent {

    @Override
    public boolean execute(JsonObject params, AbilityContext context) {
        if (!(context.getWorld() instanceof ServerWorld world) || context.getPlayer() == null) {
            return false;
        }

        int count = params.has("count") ? params.get("count").getAsInt() : 12;
        Vec3d eyePos = context.getPlayer().getEyePos();
        Vec3d look = context.getPlayer().getRotationVector();

        world.playSound(null, context.getPlayer().getBlockPos(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 2.0f, 1.0f);

        for (int i = 0; i < count; i++) {
            double spreadX = (world.getRandom().nextDouble() - 0.5) * 0.4;
            double spreadY = (world.getRandom().nextDouble() - 0.5) * 0.4;
            double spreadZ = (world.getRandom().nextDouble() - 0.5) * 0.4;
            Vec3d dir = look.add(spreadX, spreadY, spreadZ).normalize();

            ArrowEntity arrow = new ArrowEntity(world, context.getPlayer(), new ItemStack(Items.ARROW), null);
            arrow.setPosition(eyePos.x + dir.x, eyePos.y + dir.y, eyePos.z + dir.z);
            arrow.setVelocity(dir.multiply(2.5));
            arrow.setDamage(4.0);
            world.spawnEntity(arrow);
        }
        return true;
    }
}
