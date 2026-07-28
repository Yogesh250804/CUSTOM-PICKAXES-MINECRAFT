package com.ultimatepickaxes.engine.ability;

import com.ultimatepickaxes.engine.ability.components.*;

import java.util.HashMap;
import java.util.Map;

public class AbilityRegistry {
    private static final Map<String, AbilityComponent> REGISTRY = new HashMap<>();

        public static void register(String id, AbilityComponent component) {
        if (id != null) {
            REGISTRY.put(id.toLowerCase(), component);
        }
    }

    private static void reg(String id, java.util.function.Supplier<AbilityComponent> supplier) {
        try {
            AbilityComponent comp = supplier.get();
            if (comp != null) {
                REGISTRY.put(id.toLowerCase(), comp);
            }
        } catch (Throwable e) {
            REGISTRY.put(id.toLowerCase(), (params, context) -> true);
        }
    }

    public static AbilityComponent get(String id) {
        if (id == null) return null;
        return REGISTRY.get(id.toLowerCase());
    }

    public static void init() {
        reg("area_mine", () -> new AreaMiningAbility());
        reg("vein_mine", () -> new VeinMiningAbility());
        reg("bridge", () -> new BridgeAbility());
        reg("explosion", () -> new ExplosionAbility());
        reg("teleport", () -> new TeleportAbility());
        reg("freeze", () -> new FreezeAbility());
        reg("burn", () -> new BurnAbility());
        reg("item_magnet", () -> new ItemMagnetAbility());
        reg("push_entities", () -> new PushEntitiesAbility());
        reg("launch_player", () -> new LaunchPlayerAbility());
        reg("place_blocks", () -> new PlaceBlocksAbility());
        reg("status_effect", () -> new StatusEffectAbility());
        reg("summon_lightning", () -> new SummonLightningAbility());
        reg("auto_smelt", () -> new AutoSmeltAbility());
        reg("plant_growth", () -> new PlantGrowthAbility());
        reg("sonic_boom", () -> new SonicBoomAbility());
        reg("ore_sense", () -> new OreSenseAbility());
        reg("earth_wave", () -> new EarthWaveAbility());
        reg("avalanche", () -> new AvalancheAbility());

        // Custom abilities
        reg("tunnel_bore", () -> new TunnelBoreAbility());
        reg("quicksand", () -> new QuicksandAbility());
        reg("timber", () -> new TimberAbility());
        reg("root_snare", () -> new RootSnareAbility());
        reg("bone_barrage", () -> new BoneBarrageAbility());
        reg("tectonic_slam", () -> new TectonicSlamAbility());
        reg("stone_spike", () -> new StoneSpikeAbility());
        reg("petrify", () -> new PetrifyAbility());
        reg("deep_drill", () -> new DeepDrillAbility());
        reg("rockfall", () -> new RockfallAbility());
        reg("shield_dome", () -> new ShieldDomeAbility());
        reg("stalactite_rain", () -> new StalactiteRainAbility());
        reg("fortress_wall", () -> new FortressWallAbility());
        reg("brick_barrage", () -> new BrickBarrageAbility());
        reg("mud_trap", () -> new MudTrapAbility());
        reg("blizzard", () -> new BlizzardAbility());
        reg("forest_wrath", () -> new ForestWrathAbility());
        reg("fungal_bloom", () -> new FungalBloomAbility());
        reg("chain_lightning", () -> new ChainLightningAbility());
        reg("midas", () -> new MidasAbility());
        reg("stun", () -> new StunAbility());
        reg("solar_beam", () -> new SolarBeamAbility());
        reg("golem_summon", () -> new GolemSummonAbility());
        reg("soul_drain", () -> new SoulDrainAbility());
        reg("fire_trail", () -> new FireTrailAbility());
        reg("nylium_spores", () -> new NyliumSporesAbility());
        reg("lifesteal", () -> new LifestealAbility());
        reg("basalt_pillar", () -> new BasaltPillarAbility());
        reg("dark_pulse", () -> new DarkPulseAbility());
        reg("blaze_volley", () -> new BlazeVolleyAbility());
        reg("glacier_ram", () -> new GlacierRamAbility());
        reg("encase_in_ice", () -> new EncaseInIceAbility());
        reg("tidal_wave", () -> new TidalWaveAbility());
        reg("guardian_beam", () -> new GuardianBeamAbility());
        reg("time_slow", () -> new TimeSlowAbility());
        reg("bouncy_fortress", () -> new BouncyFortressAbility());
        reg("golden_feast", () -> new GoldenFeastAbility());
        reg("steak_feast", () -> new SteakFeastAbility());
        reg("food_feast", () -> new FoodFeastAbility());
        reg("sugar_rush", () -> new SugarRushAbility());
        reg("scarecrow", () -> new ScarecrowAbility());
        reg("cloud_walk", () -> new CloudWalkAbility());
        reg("spell_roulette", () -> new SpellRouletteAbility());
        reg("homing_arrow", () -> new HomingArrowAbility());
        reg("crystal_prison", () -> new CrystalPrisonAbility());
        reg("wealth_explosion", () -> new WealthExplosionAbility());
        reg("meteor_strike", () -> new MeteorStrikeAbility());
        reg("orbital_laser", () -> new OrbitalLaserAbility());
        reg("dragon_breath", () -> new DragonBreathAbility());
        reg("void_storage", () -> new VoidStorageAbility());
        reg("seismic_slam", () -> new SeismicSlamAbility());
        reg("soul_copy", () -> new SoulCopyAbility());
        reg("gravity_well", () -> new GravityWellAbility());
        reg("gravity_flip", () -> new GravityFlipAbility());
        reg("prism_beam", () -> new PrismBeamAbility());
        reg("supernova", () -> new SupernovaAbility());
        reg("headless_horseman", () -> new HeadlessHorsemanAbility());
        reg("arcane_steal", () -> new ArcaneStealAbility());
        reg("all_buffs", () -> new AllBuffsAbility());
        reg("sponge_absorb", () -> new SpongeAbsorbAbility());
        reg("thorns", () -> new ThornsAbility());

        // 7 NEW CUSTOM ABILITIES
        reg("tornado_storm", () -> new TornadoAbility());
        reg("black_hole", () -> new BlackHoleAbility());
        reg("anvil_rain", () -> new AnvilRainAbility());
        reg("volcanic_eruption", () -> new VolcanicEruptionAbility());
        reg("time_freeze", () -> new TimeFreezeAbility());
        reg("lightning_strike_volley", () -> new LightningVolleyAbility());
        reg("mob_swarm", () -> new MobSwarmAbility());

        // WILD NON-VANILLA FOOD ABILITIES
        reg("golden_overflow", () -> new GoldenOverflowAbility());
        reg("cake_explosion", () -> new CakeExplosionAbility());
        reg("cookie_frenzy", () -> new CookieFrenzyAbility());
        reg("dimensional_warp", () -> new DimensionalWarpAbility());
        reg("carnivore_rage", () -> new CarnivoreRageAbility());
        reg("golden_laser", () -> new GoldenLaserAbility());
        reg("melon_cannonade", () -> new MelonCannonadeAbility());
        reg("pumpkin_curse", () -> new PumpkinCurseAbility());
        reg("sticky_honey_trap", () -> new StickyHoneyTrapAbility());
        reg("supernova_illumination", () -> new SupernovaIlluminationAbility());
        reg("gravity_apple_drop", () -> new GravityAppleDropAbility());

        // MISSING POWER ABILITIES
        reg("meteor_shower", () -> new MeteorShowerAbility());
        reg("earthquake", () -> new EarthquakeAbility());
        reg("ice_age", () -> new IceAgeAbility());
        reg("dimensional_rift", () -> new DimensionalRiftAbility());
        reg("warden_nuke", () -> new WardenNukeAbility());
        reg("god_mode_overdrive", () -> new GodModeOverdriveAbility());
    }
}
