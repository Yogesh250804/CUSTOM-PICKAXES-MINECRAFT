package com.ultimatepickaxes.engine.progression;

import net.minecraft.nbt.NbtCompound;

public class PickaxeProgressionData {
    private int xp;
    private int level;

    public PickaxeProgressionData(int xp, int level) {
        this.xp = xp;
        this.level = level;
    }

    public static PickaxeProgressionData fromNbt(NbtCompound nbt) {
        if (nbt == null || !nbt.contains("UltimatePickaxeXP")) {
            return new PickaxeProgressionData(0, 1);
        }
        return new PickaxeProgressionData(nbt.getInt("UltimatePickaxeXP"), nbt.getInt("UltimatePickaxeLevel"));
    }

    public void writeToNbt(NbtCompound nbt) {
        nbt.putInt("UltimatePickaxeXP", xp);
        nbt.putInt("UltimatePickaxeLevel", level);
    }

    public int getXp() {
        return xp;
    }

    public int getLevel() {
        return level;
    }

    public void addXp(int amount) {
        this.xp += amount;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
