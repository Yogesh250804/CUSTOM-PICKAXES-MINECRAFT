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

        // Custom abilities
        register("tunnel_bore", new TunnelBoreAbility());
        register("quicksand", new QuicksandAbility());
        register("timber", new TimberAbility());
        register("root_snare", new RootSnareAbility());
        register("bone_barrage", new BoneBarrageAbility());
        register("tectonic_slam", new TectonicSlamAbility());
        register("stone_spike", new StoneSpikeAbility());
        register("petrify", new PetrifyAbility());
        register("deep_drill", new DeepDrillAbility());
        register("rockfall", new RockfallAbility());
        register("shield_dome", new ShieldDomeAbility());
        register("stalactite_rain", new StalactiteRainAbility());
        register("fortress_wall", new FortressWallAbility());
        register("brick_barrage", new BrickBarrageAbility());
        register("mud_trap", new MudTrapAbility());
        register("blizzard", new BlizzardAbility());
        register("forest_wrath", new ForestWrathAbility());
        register("fungal_bloom", new FungalBloomAbility());
        register("chain_lightning", new ChainLightningAbility());
        register("midas", new MidasAbility());
        register("stun", new StunAbility());
        register("solar_beam", new SolarBeamAbility());
        register("golem_summon", new GolemSummonAbility());
        register("soul_drain", new SoulDrainAbility());
        register("fire_trail", new FireTrailAbility());
        register("nylium_spores", new NyliumSporesAbility());
        register("lifesteal", new LifestealAbility());
        register("basalt_pillar", new BasaltPillarAbility());
        register("dark_pulse", new DarkPulseAbility());
        register("blaze_volley", new BlazeVolleyAbility());
        register("glacier_ram", new GlacierRamAbility());
        register("encase_in_ice", new EncaseInIceAbility());
        register("tidal_wave", new TidalWaveAbility());
        register("guardian_beam", new GuardianBeamAbility());
        register("time_slow", new TimeSlowAbility());
        register("bouncy_fortress", new BouncyFortressAbility());
        register("golden_feast", new GoldenFeastAbility());
        register("steak_feast", new SteakFeastAbility());
        register("food_feast", new FoodFeastAbility());
        register("sugar_rush", new SugarRushAbility());
        register("scarecrow", new ScarecrowAbility());
        register("cloud_walk", new CloudWalkAbility());
        register("spell_roulette", new SpellRouletteAbility());
        register("homing_arrow", new HomingArrowAbility());
        register("crystal_prison", new CrystalPrisonAbility());
        register("wealth_explosion", new WealthExplosionAbility());
        register("meteor_strike", new MeteorStrikeAbility());
        register("orbital_laser", new OrbitalLaserAbility());
        register("dragon_breath", new DragonBreathAbility());
        register("void_storage", new VoidStorageAbility());
        register("seismic_slam", new SeismicSlamAbility());
        register("soul_copy", new SoulCopyAbility());
        register("gravity_well", new GravityWellAbility());
        register("gravity_flip", new GravityFlipAbility());
        register("prism_beam", new PrismBeamAbility());
        register("supernova", new SupernovaAbility());
        register("headless_horseman", new HeadlessHorsemanAbility());
        register("arcane_steal", new ArcaneStealAbility());
        register("all_buffs", new AllBuffsAbility());
        register("sponge_absorb", new SpongeAbsorbAbility());
        register("thorns", new ThornsAbility());

        // 7 NEW CUSTOM ABILITIES
        register("tornado_storm", new TornadoAbility());
        register("black_hole", new BlackHoleAbility());
        register("anvil_rain", new AnvilRainAbility());
        register("volcanic_eruption", new VolcanicEruptionAbility());
        register("time_freeze", new TimeFreezeAbility());
        register("lightning_strike_volley", new LightningVolleyAbility());
        register("mob_swarm", new MobSwarmAbility());

        // WILD NON-VANILLA FOOD ABILITIES
        register("golden_overflow", new GoldenOverflowAbility());
        register("cake_explosion", new CakeExplosionAbility());
        register("cookie_frenzy", new CookieFrenzyAbility());
        register("dimensional_warp", new DimensionalWarpAbility());
        register("carnivore_rage", new CarnivoreRageAbility());
        register("golden_laser", new GoldenLaserAbility());
        register("melon_cannonade", new MelonCannonadeAbility());
        register("pumpkin_curse", new PumpkinCurseAbility());
        register("sticky_honey_trap", new StickyHoneyTrapAbility());
        register("supernova_illumination", new SupernovaIlluminationAbility());
        register("gravity_apple_drop", new GravityAppleDropAbility());
    }
}
