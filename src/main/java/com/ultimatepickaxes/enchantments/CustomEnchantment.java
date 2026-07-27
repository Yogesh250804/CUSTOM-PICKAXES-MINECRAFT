package com.ultimatepickaxes.enchantments;

public class CustomEnchantment {
    private final String id;
    private final int maxLevel;
    private final String description;

    public CustomEnchantment(String id, int maxLevel, String description) {
        this.id = id;
        this.maxLevel = maxLevel;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public String getDescription() {
        return description;
    }
}
