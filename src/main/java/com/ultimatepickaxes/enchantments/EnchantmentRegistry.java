package com.ultimatepickaxes.enchantments;

import java.util.HashMap;
import java.util.Map;

public class EnchantmentRegistry {
    public static final Map<String, CustomEnchantment> ENCHANTMENTS = new HashMap<>();

    public static final CustomEnchantment VEIN_BREAKER = register("vein_breaker", 3, "Breaks connected blocks of the same type.");
    public static final CustomEnchantment MAGNETISM = register("magnetism", 3, "Pulls nearby item drops to player.");
    public static final CustomEnchantment BLACK_HOLE = register("black_hole", 2, "Creates gravitational pull on mined blocks.");
    public static final CustomEnchantment THUNDER_STRIKE = register("thunder_strike", 3, "Summons lightning when mining or hitting.");
    public static final CustomEnchantment AUTO_SMELT = register("auto_smelt", 1, "Smelts mined ores into ingots automatically.");
    public static final CustomEnchantment TREASURE_HUNTER = register("treasure_hunter", 5, "Increases rare drop rates.");
    public static final CustomEnchantment GRAVITY = register("gravity", 2, "Flips entity gravity on hit.");
    public static final CustomEnchantment CRYSTAL_GROWTH = register("crystal_growth", 3, "Duplicates gem drops.");
    public static final CustomEnchantment TIME_WARP = register("time_warp", 3, "Grants sudden speed & haste bursts.");
    public static final CustomEnchantment SOUL_DRAIN = register("soul_drain", 3, "Restores health on mining/kills.");
    public static final CustomEnchantment EXPLOSION = register("explosion", 3, "Causes block break explosions.");
    public static final CustomEnchantment ORE_SENSE = register("ore_sense", 3, "Highlights nearby hidden ores.");

    private static CustomEnchantment register(String name, int maxLevel, String desc) {
        CustomEnchantment ench = new CustomEnchantment(name, maxLevel, desc);
        ENCHANTMENTS.put(name, ench);
        return ench;
    }

    public static void init() {
        // Registry loaded statically
    }
}
