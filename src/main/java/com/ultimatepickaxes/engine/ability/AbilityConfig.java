package com.ultimatepickaxes.engine.ability;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class AbilityConfig {
    private final String abilityId;
    private final JsonArray conditions;
    private final JsonObject effects;
    private final JsonObject params;

    public AbilityConfig(String abilityId, JsonArray conditions, JsonObject effects, JsonObject params) {
        this.abilityId = abilityId;
        this.conditions = conditions;
        this.effects = effects;
        this.params = params != null ? params : new JsonObject();
    }

    public static AbilityConfig fromJson(JsonObject json) {
        String id = json.has("type") ? json.get("type").getAsString() : (json.has("ability") ? json.get("ability").getAsString() : "");
        JsonArray conditions = json.has("conditions") ? json.getAsJsonArray("conditions") : null;
        JsonObject effects = json.has("effects") ? json.getAsJsonObject("effects") : null;
        JsonObject params = json.has("params") ? json.getAsJsonObject("params") : null;
        return new AbilityConfig(id, conditions, effects, params);
    }

    public String getAbilityId() {
        return abilityId;
    }

    public JsonArray getConditions() {
        return conditions;
    }

    public JsonObject getEffects() {
        return effects;
    }

    public JsonObject getParams() {
        return params;
    }
}
