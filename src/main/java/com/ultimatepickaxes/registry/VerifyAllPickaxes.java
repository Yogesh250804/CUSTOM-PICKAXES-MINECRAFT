package com.ultimatepickaxes.registry;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ultimatepickaxes.engine.ability.AbilityRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class VerifyAllPickaxes {

    private static final Logger LOGGER = LoggerFactory.getLogger("UltimatePickaxes-Validator");
    private static final Gson GSON = new Gson();

    public static void verifyAll(List<PickaxeDefinition> definitions) {
        int total = 0;
        int validAbilities = 0;
        int missingAbilities = 0;

        for (PickaxeDefinition def : definitions) {
            total++;
            if (def.getTriggers() != null) {
                for (var chain : def.getTriggers().values()) {
                    for (var config : chain) {
                        String abilityId = config.getAbilityId();
                        if (AbilityRegistry.get(abilityId) != null) {
                            validAbilities++;
                        } else {
                            missingAbilities++;
                            LOGGER.error("Pickaxe '{}' references unregistered ability: '{}'", def.getId(), abilityId);
                        }
                    }
                }
            }
        }

        LOGGER.info("================ VERIFICATION SUMMARY ================");
        LOGGER.info("Total Pickaxes Loaded & Verified: {}", total);
        LOGGER.info("Valid Abilities Verified: {}", validAbilities);
        LOGGER.info("Missing Abilities: {}", missingAbilities);
        LOGGER.info("======================================================");

        if (missingAbilities > 0) {
            throw new IllegalStateException("Verification failed! Found " + missingAbilities + " missing pickaxe ability mappings!");
        }
    }
}
