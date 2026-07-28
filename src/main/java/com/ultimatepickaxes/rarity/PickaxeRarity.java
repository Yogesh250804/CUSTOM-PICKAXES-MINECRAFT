package com.ultimatepickaxes.rarity;

import net.minecraft.util.Formatting;

public enum PickaxeRarity {
    COMMON("Common", "WHITE", false, 100),
    UNCOMMON("Uncommon", "GREEN", false, 75),
    RARE("Rare", "BLUE", false, 50),
    EPIC("Epic", "DARK_PURPLE", false, 25),
    LEGENDARY("Legendary", "GOLD", true, 10),
    MYTHIC("Mythic", "LIGHT_PURPLE", true, 5),
    DIVINE("Divine", "AQUA", true, 1);

    private final String displayName;
    private final String colorName;
    private final boolean glowing;
    private final int lootWeight;

    PickaxeRarity(String displayName, String colorName, boolean glowing, int lootWeight) {
        this.displayName = displayName;
        this.colorName = colorName;
        this.glowing = glowing;
        this.lootWeight = lootWeight;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Formatting getColor() {
        try {
            Formatting f = Formatting.byName(colorName.toLowerCase());
            return f != null ? f : Formatting.WHITE;
        } catch (Throwable e) {
            return Formatting.WHITE;
        }
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
