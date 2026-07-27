package com.ultimatepickaxes.registry;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ultimatepickaxes.engine.ability.AbilityConfig;
import com.ultimatepickaxes.engine.trigger.TriggerType;
import com.ultimatepickaxes.rarity.PickaxeRarity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PickaxeDefinition {
    private final String id;
    private final String displayName;
    private final String ingredient;
    private final int durability;
    private final float miningSpeed;
    private final float attackDamage;
    private final PickaxeRarity rarity;
    private final int cooldown;
    private final String tooltip;
    private final List<String> lore;
    private final Map<TriggerType, List<AbilityConfig>> triggers;
    private final JsonObject progressionConfig;

    public PickaxeDefinition(String id, String displayName, String ingredient, int durability, float miningSpeed, float attackDamage, PickaxeRarity rarity, int cooldown, String tooltip, List<String> lore, Map<TriggerType, List<AbilityConfig>> triggers, JsonObject progressionConfig) {
        this.id = id;
        this.displayName = displayName;
        this.ingredient = ingredient;
        this.durability = durability;
        this.miningSpeed = miningSpeed;
        this.attackDamage = attackDamage;
        this.rarity = rarity;
        this.cooldown = cooldown;
        this.tooltip = tooltip;
        this.lore = lore;
        this.triggers = triggers;
        this.progressionConfig = progressionConfig;
    }

    public static PickaxeDefinition fromJson(JsonObject json) {
        String id = json.get("id").getAsString();
        String displayName = json.has("displayName") ? json.get("displayName").getAsString() : id;
        String ingredient = json.has("ingredient") ? json.get("ingredient").getAsString() : "minecraft:cobblestone";
        int durability = json.has("durability") ? json.get("durability").getAsInt() : 500;
        float miningSpeed = json.has("miningSpeed") ? json.get("miningSpeed").getAsFloat() : 8.0f;
        float attackDamage = json.has("attackDamage") ? json.get("attackDamage").getAsFloat() : 3.0f;
        PickaxeRarity rarity = PickaxeRarity.fromString(json.has("rarity") ? json.get("rarity").getAsString() : "COMMON");
        int cooldown = json.has("cooldown") ? json.get("cooldown").getAsInt() : 60;
        String tooltip = json.has("tooltip") ? json.get("tooltip").getAsString() : "";

        List<String> lore = new ArrayList<>();
        if (json.has("lore")) {
            JsonArray loreArray = json.getAsJsonArray("lore");
            for (JsonElement el : loreArray) {
                lore.add(el.getAsString());
            }
        }

        Map<TriggerType, List<AbilityConfig>> triggers = new HashMap<>();
        if (json.has("triggers")) {
            JsonObject triggersObj = json.getAsJsonObject("triggers");
            for (Map.Entry<String, JsonElement> entry : triggersObj.entrySet()) {
                TriggerType type = TriggerType.fromString(entry.getKey());
                if (type != null && entry.getValue().isJsonArray()) {
                    List<AbilityConfig> chain = new ArrayList<>();
                    for (JsonElement abilityEl : entry.getValue().getAsJsonArray()) {
                        if (abilityEl.isJsonObject()) {
                            chain.add(AbilityConfig.fromJson(abilityEl.getAsJsonObject()));
                        }
                    }
                    triggers.put(type, chain);
                }
            }
        }

        JsonObject progressionConfig = json.has("progression") ? json.getAsJsonObject("progression") : null;

        return new PickaxeDefinition(id, displayName, ingredient, durability, miningSpeed, attackDamage, rarity, cooldown, tooltip, lore, triggers, progressionConfig);
    }

    public String getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getIngredient() {
        return ingredient;
    }

    public int getDurability() {
        return durability;
    }

    public float getMiningSpeed() {
        return miningSpeed;
    }

    public float getAttackDamage() {
        return attackDamage;
    }

    public PickaxeRarity getRarity() {
        return rarity;
    }

    public int getCooldown() {
        return cooldown;
    }

    public String getTooltip() {
        return tooltip;
    }

    public List<String> getLore() {
        return lore;
    }

    public Map<TriggerType, List<AbilityConfig>> getTriggers() {
        return triggers;
    }

    public JsonObject getProgressionConfig() {
        return progressionConfig;
    }
}
