package com.ultimatepickaxes.engine.condition;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ultimatepickaxes.engine.ability.AbilityContext;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ConditionEngine {
    private static final Map<String, Condition> CONDITIONS = new HashMap<>();
    private static final Random RANDOM = new Random();

    static {
        register("chance", (config, ctx) -> {
            double value = config.has("value") ? config.get("value").getAsDouble() : 1.0;
            return RANDOM.nextDouble() <= value;
        });

        register("health", (config, ctx) -> {
            if (ctx.getPlayer() == null) return false;
            float min = config.has("min") ? config.get("min").getAsFloat() : 0.0f;
            float max = config.has("max") ? config.get("max").getAsFloat() : Float.MAX_VALUE;
            float hp = ctx.getPlayer().getHealth();
            return hp >= min && hp <= max;
        });

        register("dimension", (config, ctx) -> {
            if (ctx.getWorld() == null) return false;
            String expected = config.has("value") ? config.get("value").getAsString() : "";
            String current = ctx.getWorld().getRegistryKey().getValue().toString();
            return current.equalsIgnoreCase(expected);
        });

        register("weather", (config, ctx) -> {
            if (ctx.getWorld() == null) return false;
            String expected = config.has("value") ? config.get("value").getAsString() : "clear";
            if (expected.equalsIgnoreCase("rain")) return ctx.getWorld().isRaining();
            if (expected.equalsIgnoreCase("thunder")) return ctx.getWorld().isThundering();
            return !ctx.getWorld().isRaining() && !ctx.getWorld().isThundering();
        });

        register("sneak", (config, ctx) -> {
            if (ctx.getPlayer() == null) return false;
            boolean expected = !config.has("value") || config.get("value").getAsBoolean();
            return ctx.getPlayer().isSneaking() == expected;
        });

        register("sprint", (config, ctx) -> {
            if (ctx.getPlayer() == null) return false;
            boolean expected = !config.has("value") || config.get("value").getAsBoolean();
            return ctx.getPlayer().isSprinting() == expected;
        });

        register("altitude", (config, ctx) -> {
            if (ctx.getPos() == null) return false;
            int minY = config.has("min") ? config.get("min").getAsInt() : Integer.MIN_VALUE;
            int maxY = config.has("max") ? config.get("max").getAsInt() : Integer.MAX_VALUE;
            int y = ctx.getPos().getY();
            return y >= minY && y <= maxY;
        });
    }

    public static void register(String type, Condition condition) {
        CONDITIONS.put(type.toLowerCase(), condition);
    }

    public static boolean evaluateAll(JsonArray conditionsArray, AbilityContext context) {
        if (conditionsArray == null || conditionsArray.size() == 0) return true;

        for (JsonElement element : conditionsArray) {
            if (!element.isJsonObject()) continue;
            JsonObject obj = element.getAsJsonObject();
            String type = obj.has("type") ? obj.get("type").getAsString().toLowerCase() : "";
            Condition condition = CONDITIONS.get(type);
            if (condition != null) {
                if (!condition.test(obj, context)) {
                    return false;
                }
            }
        }
        return true;
    }
}
