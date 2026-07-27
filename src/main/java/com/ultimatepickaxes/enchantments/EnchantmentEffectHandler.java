package com.ultimatepickaxes.enchantments;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public class EnchantmentEffectHandler {

    public static void onBlockBreak(ServerWorld world, PlayerEntity player, BlockPos pos, ItemStack stack) {
        if (stack == null || stack.isEmpty()) return;

        // Custom Magnetism effect logic
        double radius = 7.0;
        Vec3d playerPos = player.getPos();
        Box area = new Box(playerPos.x - radius, playerPos.y - radius, playerPos.z - radius, playerPos.x + radius, playerPos.y + radius, playerPos.z + radius);
        for (ItemEntity item : world.getEntitiesByClass(ItemEntity.class, area, e -> true)) {
            item.setPosition(playerPos.x, playerPos.y + 0.5, playerPos.z);
        }

        // Custom Soul Drain effect logic
        player.heal(0.5f);
    }
}
