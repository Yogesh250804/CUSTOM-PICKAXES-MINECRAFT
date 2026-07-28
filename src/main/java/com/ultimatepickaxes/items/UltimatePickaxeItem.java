package com.ultimatepickaxes.items;

import com.ultimatepickaxes.engine.ability.AbilityContext;
import com.ultimatepickaxes.engine.trigger.TriggerDispatcher;
import com.ultimatepickaxes.engine.trigger.TriggerType;
import com.ultimatepickaxes.registry.PickaxeDefinition;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class UltimatePickaxeItem extends PickaxeItem {
    private final PickaxeDefinition definition;

    public UltimatePickaxeItem(PickaxeDefinition definition, ToolMaterial material, Settings settings) {
        super(material, settings);
        this.definition = definition;
    }

    public PickaxeDefinition getDefinition() {
        return definition;
    }

    @Override
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        boolean superResult = super.postMine(stack, world, state, pos, miner);
        if (!world.isClient && miner instanceof PlayerEntity player) {
            AbilityContext ctx = new AbilityContext(world, player, stack, pos, null, null, null, TriggerType.ON_MINE);
            TriggerDispatcher.dispatch(TriggerType.ON_MINE, ctx);
        }
        return superResult;
    }

    @Override
    public Text getName(ItemStack stack) {
        if (definition != null && definition.getDisplayName() != null) {
            return Text.literal(definition.getDisplayName());
        }
        return super.getName(stack);
    }

    @Override
    public net.minecraft.util.ActionResult useOnBlock(net.minecraft.item.ItemUsageContext context) {
        World world = context.getWorld();
        PlayerEntity user = context.getPlayer();
        if (user != null && !world.isClient) {
            ItemStack stack = context.getStack();
            AbilityContext ctx = new AbilityContext(world, user, stack, context.getBlockPos(), null, null, null, TriggerType.ON_RIGHT_CLICK);
            boolean executed = TriggerDispatcher.dispatch(TriggerType.ON_RIGHT_CLICK, ctx);
            if (executed) {
                return net.minecraft.util.ActionResult.SUCCESS;
            }
        }
        return net.minecraft.util.ActionResult.PASS;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (!world.isClient) {
            AbilityContext ctx = new AbilityContext(world, user, stack, user.getBlockPos(), null, null, null, TriggerType.ON_RIGHT_CLICK);
            boolean executed = TriggerDispatcher.dispatch(TriggerType.ON_RIGHT_CLICK, ctx);
            if (executed) {
                return TypedActionResult.success(stack);
            }
        }
        return TypedActionResult.pass(stack);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!attacker.getWorld().isClient && attacker instanceof PlayerEntity player) {
            AbilityContext ctx = new AbilityContext(attacker.getWorld(), player, stack, target.getBlockPos(), target, null, null, TriggerType.ON_HIT_ENTITY);
            TriggerDispatcher.dispatch(TriggerType.ON_HIT_ENTITY, ctx);
        }
        return super.postHit(stack, target, attacker);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (!world.isClient && selected && entity instanceof PlayerEntity player) {
            AbilityContext ctx = new AbilityContext(world, player, stack, player.getBlockPos(), null, null, null, TriggerType.ON_TICK);
            TriggerDispatcher.dispatch(TriggerType.ON_TICK, ctx);
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        String abilityName = getAbilityTitle(definition.getId());
        tooltip.add(Text.literal(abilityName).formatted(Formatting.GOLD, Formatting.BOLD));

        String desc = getAbilityDescription();
        if (!desc.isEmpty()) {
            tooltip.add(Text.literal(desc).formatted(Formatting.GRAY));
        }
    }

    private String getAbilityDescription() {
        if (definition != null) {
            String rawTooltip = definition.getTooltip();
            if (rawTooltip != null && !rawTooltip.isEmpty()
                    && !rawTooltip.contains("elemental power")
                    && !rawTooltip.contains("special power")
                    && !rawTooltip.equalsIgnoreCase("Grants special elemental power.")) {
                return rawTooltip;
            }

            if (definition.getTriggers() != null) {
                var list = definition.getTriggers().get(TriggerType.ON_RIGHT_CLICK);
                if (list != null && !list.isEmpty()) {
                    String type = list.get(0).getAbilityId();
                    return switch (type) {
                        case "quicksand" -> "Traps surrounding enemies in slowing quicksand.";
                        case "soul_drain" -> "Drains health from nearby enemies to heal you.";
                        case "golden_feast" -> "Restores full hunger & grants Absorption, Regeneration, and Resistance.";
                        case "steak_feast" -> "Restores full hunger & grants Strength and Saturation.";
                        case "sugar_rush" -> "Grants extreme speed and haste boost.";
                        case "earth_wave" -> "Launches a wave of earth damaging enemies ahead.";
                        case "timber" -> "Chops down an entire tree instantly.";
                        case "tunnel_bore" -> "Bores a long mining tunnel ahead.";
                        case "explosion" -> "Launches an explosive projectile.";
                        case "crystal_prison" -> "Traps nearby enemies inside amethyst crystals.";
                        case "meteor_strike" -> "Summons a flaming meteor from the sky.";
                        case "meteor_shower" -> "Rains a catastrophic meteor shower upon your location.";
                        case "orbital_laser" -> "Fires an intense orbital laser beam from above.";
                        case "dragon_breath" -> "Breathes persistent dragon fire damaging targets.";
                        case "sonic_boom" -> "Emits a high-damage Warden sonic boom forward.";
                        case "black_hole" -> "Creates a singularity pulling in nearby mobs and objects.";
                        case "solar_flare" -> "Blinds and burns enemies with solar flare.";
                        case "solar_beam" -> "Fires a beam of concentrated solar energy.";
                        case "dimensional_rift" -> "Teleports you forward while leaving a temporal rift.";
                        case "earthquake" -> "Shakes the ground to stun and damage surrounding mobs.";
                        case "ice_age" -> "Freezes the battlefield and surrounding mobs solid.";
                        case "blizzard" -> "Summons a freezing blizzard slowing and damaging enemies.";
                        case "tornado_storm" -> "Spawns a powerful tornado sweeping mobs away.";
                        case "anvil_rain" -> "Rains heavy anvils from above onto targets.";
                        case "volcanic_eruption" -> "Erupts molten lava and magma blocks around you.";
                        case "time_freeze" -> "Freezes time and immobilizes all nearby entities.";
                        case "lightning_strike_volley" -> "Summons multiple lightning strikes around you.";
                        case "mob_swarm" -> "Summons a swarm of undead minions to fight for you.";
                        case "chain_lightning" -> "Strikes targets with lightning chaining to nearby enemies.";
                        case "midas" -> "Converts nearby stone into valuable ores or golden blocks.";
                        case "tectonic_slam" -> "Slams the ground sending shocks in all directions.";
                        case "stone_spike" -> "Spawns sharp stone spikes impaling enemies.";
                        case "petrify" -> "Turns nearby enemies into solid stone temporarily.";
                        case "deep_drill" -> "Drills deep down into bedrock creating vertical shafts.";
                        case "rockfall" -> "Drops heavy rocks on top of target location.";
                        case "shield_dome" -> "Creates a protective energy dome around you.";
                        case "stalactite_rain" -> "Drops sharp pointed dripstones from above.";
                        case "fortress_wall" -> "Raises a defensive wall in front of you.";
                        case "brick_barrage" -> "Fires a rapid barrage of heavy brick projectiles.";
                        case "mud_trap" -> "Creates sticky mud trapping enemies in place.";
                        case "forest_wrath" -> "Summons thorny roots and leaves entangling foes.";
                        case "fungal_bloom" -> "Spreads toxic fungal spores poisoning nearby mobs.";
                        case "golem_summon" -> "Summons a protective Golem to defend you.";
                        case "fire_trail" -> "Leaves a burning trail of fire behind you.";
                        case "nylium_spores" -> "Spreads Nether spores causing decay and confusion.";
                        case "basalt_pillar" -> "Raises basalt pillars beneath you or targets.";
                        case "dark_pulse" -> "Emits a pulse of darkness damaging and blinding mobs.";
                        case "blaze_volley" -> "Fires a volley of blaze fireballs forward.";
                        case "glacier_ram" -> "Charges forward inside a glacial ice shield.";
                        case "encase_in_ice" -> "Encases nearby enemies in solid ice blocks.";
                        case "tidal_wave" -> "Summons a rushing wave of water knocking back foes.";
                        case "guardian_beam" -> "Fires a targeting beam dealing continuous damage.";
                        case "time_slow" -> "Slows down time for surrounding enemies.";
                        case "bouncy_fortress" -> "Launches you skyward with super slime bounce.";
                        case "scarecrow" -> "Places a ward scaring away aggressive mobs.";
                        case "cloud_walk" -> "Allows walking on air temporary solid clouds.";
                        case "spell_roulette" -> "Casts a random powerful magic spell.";
                        case "homing_arrow" -> "Fires seeking arrows that track nearby enemies.";
                        case "wealth_explosion" -> "Explodes into a shower of shiny gems and XP.";
                        case "void_storage" -> "Opens your personal portable ender void storage.";
                        case "seismic_slam" -> "Slams into the ground generating shockwaves.";
                        case "gravity_well" -> "Creates a gravity well crushing mobs inward.";
                        case "gravity_flip" -> "Flips gravity sending nearby mobs flying up.";
                        case "prism_beam" -> "Fires a rainbow laser dealing heavy damage.";
                        case "supernova" -> "Triggers a blinding supernova flash damaging all nearby.";
                        case "sponge_absorb" -> "Siphons water and liquid from surrounding area.";
                        case "thorns" -> "Reflects damage back to attackers with cactus thorns.";
                        case "plant_growth" -> "Instantly grows crops and plants around you.";
                        case "area_mine" -> "Mines blocks in a 3x3 radius around broken block.";
                        case "vein_mine" -> "Mines the entire connected vein of matching blocks.";
                        case "bridge" -> "Constructs a bridge path beneath your feet.";
                        default -> "Activates the " + type.replace("_", " ") + " ability.";
                    };
                }
            }
        }
        return "";
    }

    private String getAbilityTitle(String id) {
        if (definition != null && definition.getTriggers() != null) {
            var list = definition.getTriggers().get(TriggerType.ON_RIGHT_CLICK);
            if (list != null && !list.isEmpty()) {
                String type = list.get(0).getAbilityId();
                return switch (type) {
                    case "earth_wave" -> "⚡ Earth Wave";
                    case "timber" -> "⚡ Timber Strike";
                    case "tunnel_bore" -> "⚡ Tunnel Bore";
                    case "explosion" -> "⚡ Cluster Bomb";
                    case "crystal_prison" -> "⚡ Crystal Prison";
                    case "meteor_strike" -> "⚡ Meteor Strike";
                    case "meteor_shower" -> "⚡ Meteor Cataclysm";
                    case "orbital_laser" -> "⚡ Orbital Laser";
                    case "dragon_breath" -> "⚡ Dragon Breath";
                    case "dragon_death_ray" -> "⚡ Dragon Death Ray";
                    case "warden_nuke" -> "⚡ Warden Sonic Nuke";
                    case "sonic_boom" -> "⚡ Sonic Pulse";
                    case "black_hole" -> "⚡ Singularity Void";
                    case "solar_flare" -> "⚡ Solar Flare";
                    case "solar_beam" -> "⚡ Solar Beam";
                    case "dimensional_rift" -> "⚡ Dimensional Rift";
                    case "earthquake" -> "⚡ Seismic Earthquake";
                    case "ice_age" -> "⚡ Glacial Ice Age";
                    case "blizzard" -> "⚡ Blizzard Storm";
                    case "tornado_storm" -> "⚡ Tornado Storm";
                    case "anvil_rain" -> "⚡ Anvil Rain";
                    case "volcanic_eruption" -> "⚡ Volcanic Eruption";
                    case "time_freeze" -> "⚡ Time Freeze";
                    case "lightning_strike_volley" -> "⚡ Lightning Volley";
                    case "mob_swarm" -> "⚡ Undead Mob Swarm";
                    case "shadow_army" -> "⚡ Shadow Army";
                    case "gravity_cataclysm" -> "⚡ Anti-Gravity Cataclysm";
                    case "god_mode_overdrive" -> "⚡ God Mode Overdrive";
                    case "chain_lightning" -> "⚡ Chain Lightning";
                    case "midas" -> "⚡ Midas Touch";
                    case "tectonic_slam" -> "⚡ Tectonic Slam";
                    case "stone_spike" -> "⚡ Stone Spikes";
                    case "petrify" -> "⚡ Petrify";
                    case "deep_drill" -> "⚡ Deep Drill";
                    case "rockfall" -> "⚡ Rockfall";
                    case "shield_dome" -> "⚡ Shield Dome";
                    case "stalactite_rain" -> "⚡ Stalactite Rain";
                    case "fortress_wall" -> "⚡ Fortress Wall";
                    case "brick_barrage" -> "⚡ Brick Barrage";
                    case "mud_trap" -> "⚡ Mud Trap";
                    case "forest_wrath" -> "⚡ Forest Wrath";
                    case "fungal_bloom" -> "⚡ Fungal Bloom";
                    case "golem_summon" -> "⚡ Golem Summon";
                    case "soul_drain" -> "⚡ Soul Drain";
                    case "fire_trail" -> "⚡ Fire Trail";
                    case "nylium_spores" -> "⚡ Nether Spores";
                    case "basalt_pillar" -> "⚡ Basalt Pillar";
                    case "dark_pulse" -> "⚡ Dark Pulse";
                    case "blaze_volley" -> "⚡ Blaze Volley";
                    case "glacier_ram" -> "⚡ Glacier Ram";
                    case "encase_in_ice" -> "⚡ Encase In Ice";
                    case "tidal_wave" -> "⚡ Tidal Wave";
                    case "guardian_beam" -> "⚡ Guardian Beam";
                    case "time_slow" -> "⚡ Time Slow";
                    case "bouncy_fortress" -> "⚡ Super Slime Bounce";
                    case "golden_feast" -> "⚡ Golden Feast";
                    case "steak_feast" -> "⚡ Hearty Steak Surge";
                    case "sugar_rush" -> "⚡ Sugar Rush";
                    case "scarecrow" -> "⚡ Scarecrow Zone";
                    case "cloud_walk" -> "⚡ Cloud Walk";
                    case "spell_roulette" -> "⚡ Spell Roulette";
                    case "homing_arrow" -> "⚡ Homing Arrow Storm";
                    case "wealth_explosion" -> "⚡ Wealth Explosion";
                    case "void_storage" -> "⚡ Void Storage";
                    case "seismic_slam" -> "⚡ Seismic Slam";
                    case "gravity_well" -> "⚡ Gravity Well";
                    case "gravity_flip" -> "⚡ Gravity Flip";
                    case "prism_beam" -> "⚡ Prism Beam";
                    case "supernova" -> "⚡ Supernova Flash";
                    case "headless_horseman" -> "⚡ Headless Horseman";
                    case "arcane_steal" -> "⚡ Arcane Pulse";
                    case "all_buffs" -> "⚡ All Buffs";
                    case "sponge_absorb" -> "⚡ Water Siphon";
                    case "thorns" -> "⚡ Cactus Thorns";
                    case "plant_growth" -> "⚡ Plant Growth";
                    case "area_mine" -> "⚡ Area Mine (3x3)";
                    case "vein_mine" -> "⚡ Vein Mine";
                    case "bridge" -> "⚡ Bridge Builder";
                    default -> "⚡ " + type.replace("_", " ").toUpperCase();
                };
            }
        }
        return "⚡ Custom Power";
    }
}
