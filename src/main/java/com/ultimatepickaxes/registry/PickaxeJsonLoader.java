package com.ultimatepickaxes.registry;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class PickaxeJsonLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger("UltimatePickaxes");
    private static final Gson GSON = new Gson();

    public static List<PickaxeDefinition> loadAll() {
        List<PickaxeDefinition> definitions = new ArrayList<>();

        try {
            Path dataPath = null;
            try {
                if (FabricLoader.getInstance() != null && FabricLoader.getInstance().getModContainer("ultimatepickaxes").isPresent()) {
                    dataPath = FabricLoader.getInstance().getModContainer("ultimatepickaxes").get().findPath("data/ultimatepickaxes/pickaxes").orElse(null);
                }
            } catch (Throwable ignored) {}

            if (dataPath == null || !Files.exists(dataPath)) {
                dataPath = Path.of("src/main/resources/data/ultimatepickaxes/pickaxes");
            }

            if (Files.exists(dataPath)) {
                try (Stream<Path> stream = Files.walk(dataPath)) {
                    stream.filter(p -> p.toString().endsWith(".json")).forEach(p -> {
                        try (InputStream is = Files.newInputStream(p)) {
                            InputStreamReader reader = new InputStreamReader(is, StandardCharsets.UTF_8);
                            JsonObject json = GSON.fromJson(reader, JsonObject.class);
                            PickaxeDefinition def = PickaxeDefinition.fromJson(json);
                            definitions.add(def);
                        } catch (Exception e) {
                            LOGGER.error("Failed to parse Pickaxe JSON: {}", p, e);
                        }
                    });
                }
            }
        } catch (Exception e) {
            LOGGER.error("Failed to load pickaxe definitions dynamically", e);
        }

        return definitions;
    }
}
