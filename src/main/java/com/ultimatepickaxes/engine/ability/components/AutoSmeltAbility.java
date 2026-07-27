package com.ultimatepickaxes.engine.ability.components;

import com.google.gson.JsonObject;
import com.ultimatepickaxes.engine.ability.AbilityComponent;
import com.ultimatepickaxes.engine.ability.AbilityContext;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;

public class AutoSmeltAbility implements AbilityComponent {

    @Override
    public boolean execute(JsonObject params, AbilityContext context) {
        if (!(context.getWorld() instanceof ServerWorld world) || context.getPos() == null) {
            return false;
        }

        Block brokenBlock = world.getBlockState(context.getPos()).getBlock();
        Item resultItem = null;

        if (brokenBlock == Blocks.IRON_ORE || brokenBlock == Blocks.DEEPSLATE_IRON_ORE || brokenBlock == Blocks.RAW_IRON_BLOCK) {
            resultItem = Items.IRON_INGOT;
        } else if (brokenBlock == Blocks.GOLD_ORE || brokenBlock == Blocks.DEEPSLATE_GOLD_ORE || brokenBlock == Blocks.RAW_GOLD_BLOCK) {
            resultItem = Items.GOLD_INGOT;
        } else if (brokenBlock == Blocks.COPPER_ORE || brokenBlock == Blocks.DEEPSLATE_COPPER_ORE) {
            resultItem = Items.COPPER_INGOT;
        } else if (brokenBlock == Blocks.SAND) {
            resultItem = Items.GLASS;
        } else if (brokenBlock == Blocks.COBBLESTONE) {
            resultItem = Items.STONE;
        }

        if (resultItem != null) {
            world.breakBlock(context.getPos(), false, context.getPlayer());
            Block.dropStack(world, context.getPos(), new ItemStack(resultItem));
            return true;
        }
        return false;
    }
}
