package com.ultimatepickaxes.engine.condition;

import com.google.gson.JsonObject;
import com.ultimatepickaxes.engine.ability.AbilityContext;

public interface Condition {
    boolean test(JsonObject jsonConfig, AbilityContext context);
}
