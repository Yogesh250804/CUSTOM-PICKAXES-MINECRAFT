package com.ultimatepickaxes.items;

import net.minecraft.block.Block;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;

public class DynamicToolMaterial implements ToolMaterial {
    private final int durability;
    private final float miningSpeed;
    private final float attackDamage;
    private final Ingredient repairIngredient;

    public DynamicToolMaterial(int durability, float miningSpeed, float attackDamage, Ingredient repairIngredient) {
        this.durability = durability;
        this.miningSpeed = miningSpeed;
        this.attackDamage = attackDamage;
        this.repairIngredient = repairIngredient != null ? repairIngredient : Ingredient.EMPTY;
    }

    @Override
    public int getDurability() {
        return durability;
    }

    @Override
    public float getMiningSpeedMultiplier() {
        return miningSpeed;
    }

    @Override
    public float getAttackDamage() {
        return attackDamage;
    }

    @Override
    public TagKey<Block> getInverseTag() {
        return BlockTags.INCORRECT_FOR_NETHERITE_TOOL;
    }

    @Override
    public int getEnchantability() {
        return 22;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return repairIngredient;
    }
}
