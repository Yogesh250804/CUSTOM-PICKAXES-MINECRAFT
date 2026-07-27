package com.ultimatepickaxes.engine.ability;

import com.google.gson.JsonObject;

public interface AbilityComponent {
    boolean execute(JsonObject params, AbilityContext context);
}
