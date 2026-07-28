package com.ultimatepickaxes.registry;

import com.ultimatepickaxes.engine.ability.AbilityRegistry;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class VerifyAllPickaxes {

    private static final Logger LOGGER = LoggerFactory.getLogger("UltimatePickaxes-Validator");

    public static void verifyAll(List<PickaxeDefinition> definitions) {
        // Ensure AbilityRegistry is initialized
        AbilityRegistry.init();

        int total = 0;
        int validAbilities = 0;
        int missingAbilities = 0;
        int validTextures = 0;
        int missingTextures = 0;

        Path itemTexturesPath = null;
        try {
            if (FabricLoader.getInstance() != null && FabricLoader.getInstance().getModContainer("ultimatepickaxes").isPresent()) {
                itemTexturesPath = FabricLoader.getInstance().getModContainer("ultimatepickaxes").get().findPath("assets/ultimatepickaxes/textures/item").orElse(null);
            }
        } catch (Throwable ignored) {}

        if (itemTexturesPath == null || !Files.exists(itemTexturesPath)) {
            itemTexturesPath = Path.of("src/main/resources/assets/ultimatepickaxes/textures/item");
        }

        for (PickaxeDefinition def : definitions) {
            total++;

            // 1. Verify Abilities
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

            // 2. Verify Textures
            if (itemTexturesPath != null && Files.exists(itemTexturesPath)) {
                Path texFile = itemTexturesPath.resolve(def.getId() + ".png");
                if (Files.exists(texFile)) {
                    validTextures++;
                } else {
                    missingTextures++;
                    LOGGER.error("Pickaxe '{}' missing direct texture at assets/ultimatepickaxes/textures/item/{}.png", def.getId(), def.getId());
                }
            }
        }

        LOGGER.info("================ VERIFICATION SUMMARY ================");
        LOGGER.info("Total Pickaxes Loaded & Verified: {}", total);
        LOGGER.info("Valid Abilities Verified: {}", validAbilities);
        LOGGER.info("Missing Abilities: {}", missingAbilities);
        LOGGER.info("Valid Textures Verified: {}", validTextures);
        LOGGER.info("Missing Textures: {}", missingTextures);
        LOGGER.info("======================================================");

        System.out.println("================ VERIFICATION SUMMARY ================");
        System.out.println("Total Pickaxes Loaded & Verified: " + total);
        System.out.println("Valid Abilities Verified: " + validAbilities);
        System.out.println("Missing Abilities: " + missingAbilities);
        System.out.println("Valid Textures Verified: " + validTextures);
        System.out.println("Missing Textures: " + missingTextures);
        System.out.println("======================================================");

        if (missingAbilities > 0 || missingTextures > 0) {
            throw new IllegalStateException("Verification failed! Found " + missingAbilities + " missing ability mappings and " + missingTextures + " missing texture files!");
        }
    }
}
