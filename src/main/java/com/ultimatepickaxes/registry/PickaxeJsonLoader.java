package com.ultimatepickaxes.registry;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class PickaxeJsonLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger("UltimatePickaxes");
    private static final Gson GSON = new Gson();

    public static final String[] DEFAULT_PICKAXE_FILES = {
        "tnt_pickaxe.json", "glass_pickaxe.json", "sponge_pickaxe.json", "honey_pickaxe.json",
        "magma_pickaxe.json", "slime_pickaxe.json", "coal_pickaxe.json", "diamond_pickaxe.json",
        "dragon_egg_pickaxe.json", "dirt_pickaxe.json", "cobblestone_pickaxe.json", "amethyst_pickaxe.json",
        "redstone_pickaxe.json", "gold_pickaxe.json", "iron_pickaxe.json", "obsidian_pickaxe.json",
        "glowstone_pickaxe.json", "netherite_pickaxe.json", "apple_pickaxe.json", "pumpkin_pickaxe.json",
        "cake_pickaxe.json", "sand_pickaxe.json", "gravel_pickaxe.json", "sculk_pickaxe.json",
        "wood_pickaxe.json", "lapis_pickaxe.json", "emerald_pickaxe.json", "prismarine_pickaxe.json",
        "end_stone_pickaxe.json", "shulker_pickaxe.json"
    };

    public static List<PickaxeDefinition> loadAll() {
        List<PickaxeDefinition> definitions = new ArrayList<>();
        ClassLoader classLoader = PickaxeJsonLoader.class.getClassLoader();

        for (String file : DEFAULT_PICKAXE_FILES) {
            String path = "data/ultimatepickaxes/pickaxes/" + file;
            try (InputStream stream = classLoader.getResourceAsStream(path)) {
                if (stream != null) {
                    InputStreamReader reader = new InputStreamReader(stream, StandardCharsets.UTF_8);
                    JsonObject json = GSON.fromJson(reader, JsonObject.class);
                    PickaxeDefinition def = PickaxeDefinition.fromJson(json);
                    definitions.add(def);
                    LOGGER.info("Loaded Pickaxe JSON definition: {}", def.getId());
                } else {
                    LOGGER.warn("Could not find Pickaxe JSON file resource: {}", path);
                }
            } catch (Exception e) {
                LOGGER.error("Failed to parse Pickaxe JSON: {}", path, e);
            }
        }
        return definitions;
    }
}
