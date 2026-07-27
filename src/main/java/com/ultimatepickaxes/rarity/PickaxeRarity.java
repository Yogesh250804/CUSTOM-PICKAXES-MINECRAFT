package com.ultimatepickaxes.rarity;

import net.minecraft.util.Formatting;

public enum PickaxeRarity {
    COMMON("Common", Formatting.WHITE, false, 100),
    UNCOMMON("Uncommon", Formatting.GREEN, false, 75),
    RARE("Rare", Formatting.BLUE, false, 50),
    EPIC("Epic", Formatting.DARK_PURPLE, false, 25),
    LEGENDARY("Legendary", Formatting.GOLD, true, 10),
    MYTHIC("Mythic", Formatting.LIGHT_PURPLE, true, 5),
    DIVINE("Divine", Formatting.AQUA, true, 1);

    private final String displayName;
    private final Formatting color;
    private final boolean glowing;
    private final int lootWeight;

    PickaxeRarity(String displayName, Formatting color, boolean glowing, int lootWeight) {
        this.displayName = displayName;
        this.color = color;
        this.glowing = glowing;
        this.lootWeight = lootWeight;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Formatting getColor() {
        return color;
    }

    public boolean isGlowing() {
        return glowing;
    }

    public int getLootWeight() {
        return lootWeight;
    }

    public static PickaxeRarity fromString(String name) {
        if (name == null) return COMMON;
        try {
            return PickaxeRarity.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            return COMMON;
        }
    }
}
