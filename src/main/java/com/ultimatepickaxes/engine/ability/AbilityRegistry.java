package com.ultimatepickaxes.engine.ability;

import com.ultimatepickaxes.engine.ability.components.*;

import java.util.HashMap;
import java.util.Map;

public class AbilityRegistry {
    private static final Map<String, AbilityComponent> REGISTRY = new HashMap<>();

    public static void register(String id, AbilityComponent component) {
        REGISTRY.put(id.toLowerCase(), component);
    }

    public static AbilityComponent get(String id) {
        if (id == null) return null;
        return REGISTRY.get(id.toLowerCase());
    }

    public static void init() {
        register("area_mine", new AreaMiningAbility());
        register("vein_mine", new VeinMiningAbility());
        register("bridge", new BridgeAbility());
        register("explosion", new ExplosionAbility());
        register("teleport", new TeleportAbility());
        register("freeze", new FreezeAbility());
        register("burn", new BurnAbility());
        register("item_magnet", new ItemMagnetAbility());
        register("push_entities", new PushEntitiesAbility());
        register("launch_player", new LaunchPlayerAbility());
        register("place_blocks", new PlaceBlocksAbility());
        register("status_effect", new StatusEffectAbility());
        register("summon_lightning", new SummonLightningAbility());
        register("auto_smelt", new AutoSmeltAbility());
        register("plant_growth", new PlantGrowthAbility());
        register("sonic_boom", new SonicBoomAbility());
        register("ore_sense", new OreSenseAbility());
        register("earth_wave", new EarthWaveAbility());
        register("avalanche", new AvalancheAbility());
    }
}
