package com.ultimatepickaxes.engine.trigger;

public enum TriggerType {
    ON_MINE("onMine"),
    ON_RIGHT_CLICK("onRightClick"),
    ON_TICK("onTick"),
    ON_HIT_ENTITY("onHitEntity"),
    ON_KILL("onKill"),
    ON_SNEAK("onSneak"),
    ON_SPRINT("onSprint"),
    ON_JUMP("onJump"),
    ON_FALL("onFall"),
    ON_DAMAGE_TAKEN("onDamageTaken"),
    ON_BLOCK_PLACE("onBlockPlace"),
    ON_DIMENSION_CHANGE("onDimensionChange"),
    ON_EQUIP("onEquip"),
    ON_UNEQUIP("onUnequip"),
    ON_BREAK_TOOL("onBreakTool");

    private final String jsonKey;

    TriggerType(String jsonKey) {
        this.jsonKey = jsonKey;
    }

    public String getJsonKey() {
        return jsonKey;
    }

    public static TriggerType fromString(String key) {
        if (key == null) return null;
        for (TriggerType type : values()) {
            if (type.jsonKey.equalsIgnoreCase(key) || type.name().equalsIgnoreCase(key)) {
                return type;
            }
        }
        return null;
    }
}
