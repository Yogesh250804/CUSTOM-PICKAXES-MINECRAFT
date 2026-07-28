package com.ultimatepickaxes;

import com.ultimatepickaxes.engine.ability.AbilityRegistry;
import com.ultimatepickaxes.engine.effect.ScreenShakePayload;
import com.ultimatepickaxes.enchantments.EnchantmentRegistry;
import com.ultimatepickaxes.registry.PickaxeRegistry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UltimatePickaxes implements ModInitializer {
    public static final String MOD_ID = "ultimatepickaxes";
    public static final Logger LOGGER = LoggerFactory.getLogger("UltimatePickaxes");

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing Ultimate Pickaxes Engine...");

        // Register custom networking payload
        PayloadTypeRegistry.playS2C().register(ScreenShakePayload.ID, ScreenShakePayload.CODEC);

        // 1. Initialize Parametric Ability Component Engine
        AbilityRegistry.init();
        com.ultimatepickaxes.engine.ability.components.UndeadAllyProtectionHandler.init();

        // 2. Initialize Custom Enchantments
        EnchantmentRegistry.init();

        // 3. Discover and Register JSON Pickaxes & Creative Tab
        PickaxeRegistry.init();

        LOGGER.info("Ultimate Pickaxes Engine successfully booted with {} JSON Pickaxes registered!", PickaxeRegistry.PICKAXES.size());
    }
}
