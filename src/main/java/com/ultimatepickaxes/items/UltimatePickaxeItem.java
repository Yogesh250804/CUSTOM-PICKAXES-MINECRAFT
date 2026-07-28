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
        // Display ONLY:
        // ⚡ Ability Name
        // One short exciting sentence

        String abilityName = getAbilityTitle(definition.getId());
        tooltip.add(Text.literal(abilityName).formatted(Formatting.GOLD, Formatting.BOLD));

        if (!definition.getTooltip().isEmpty()) {
            tooltip.add(Text.literal(definition.getTooltip()).formatted(Formatting.GRAY));
        }
    }

    private String getAbilityTitle(String id) {
        return switch (id) {
            case "dirt_pickaxe" -> "⚡ Earth Wave";
            case "cobblestone_pickaxe" -> "⚡ Tunnel Bore";
            case "tnt_pickaxe" -> "⚡ Cluster Bomb";
            case "glass_pickaxe" -> "⚡ Prism Beam";
            case "sponge_pickaxe" -> "⚡ Water Siphon";
            case "cactus_pickaxe" -> "⚡ Cactus Thorns";
            case "honey_pickaxe" -> "⚡ Time Slow";
            case "magma_pickaxe" -> "⚡ Flame Eruption";
            case "slime_pickaxe" -> "⚡ Super Slime Bounce";
            case "coal_pickaxe" -> "⚡ Shadow Dash";
            case "diamond_pickaxe" -> "⚡ Crystal Prison";
            case "dragon_egg_pickaxe" -> "⚡ Dragon Breath";
            case "amethyst_pickaxe" -> "⚡ Sonic Shockwave";
            case "redstone_pickaxe" -> "⚡ Stun EMP";
            case "gold_pickaxe" -> "⚡ Midas Touch";
            case "iron_pickaxe" -> "⚡ Magnet Storm";
            case "obsidian_pickaxe" -> "⚡ Void Shield";
            case "glowstone_pickaxe" -> "⚡ Supernova Flash";
            case "netherite_pickaxe" -> "⚡ Meteor Strike";
            case "apple_pickaxe" -> "⚡ Golden Feast";
            case "pumpkin_pickaxe" -> "⚡ Headless Horseman";
            case "cake_pickaxe" -> "⚡ Sugar Rush";
            case "sand_pickaxe" -> "⚡ Quicksand Trap";
            case "gravel_pickaxe" -> "⚡ Avalanche";
            case "sculk_pickaxe" -> "⚡ Warden Sonic Pulse";
            case "wood_pickaxe" -> "⚡ Timber Strike";
            case "lapis_pickaxe" -> "⚡ Arcane Pulse";
            case "emerald_pickaxe" -> "⚡ Wealth Explosion";
            case "prismarine_pickaxe" -> "⚡ Tidal Wave";
            case "end_stone_pickaxe" -> "⚡ Gravity Flip";
            case "shulker_pickaxe" -> "⚡ Gravity Well";
            case "beacon_pickaxe" -> "⚡ Orbital Laser";
            case "bookshelf_pickaxe" -> "⚡ Spell Roulette";
            case "copper_pickaxe" -> "⚡ Chain Lightning";
            case "ender_chest_pickaxe" -> "⚡ Void Storage";
            case "bedrock_pickaxe" -> "⚡ Seismic Slam";
            case "crying_obsidian_pickaxe" -> "⚡ Soul Tear";
            case "hay_bale_pickaxe" -> "⚡ Scarecrow Zone";
            case "ice_pickaxe" -> "⚡ Frost Nova";
            case "moss_pickaxe" -> "⚡ Root Snare";
            case "nylium_pickaxe" -> "⚡ Nether Spores";
            case "prismarine_bricks_pickaxe" -> "⚡ Guardian Beam";
            case "purpur_pickaxe" -> "⚡ Chorus Warp";
            case "quartz_pickaxe" -> "⚡ Solar Beam";
            case "soul_sand_pickaxe" -> "⚡ Soul Drain";
            case "target_pickaxe" -> "⚡ Homing Arrow Storm";
            case "terracotta_pickaxe" -> "⚡ Clay Golem";
            case "warped_pickaxe" -> "⚡ Warped Teleport";
            case "wool_pickaxe" -> "⚡ Cloud Walk";
            case "bone_pickaxe" -> "⚡ Bone Barrage";
            case "enchanting_table_pickaxe" -> "⚡ Arcane Overload";
            // New pickaxes
            case "granite_pickaxe" -> "⚡ Tectonic Slam";
            case "diorite_pickaxe" -> "⚡ Stone Spikes";
            case "andesite_pickaxe" -> "⚡ Petrify";
            case "deepslate_pickaxe" -> "⚡ Deep Drill";
            case "tuff_pickaxe" -> "⚡ Rockfall";
            case "calcite_pickaxe" -> "⚡ Shield Dome";
            case "dripstone_pickaxe" -> "⚡ Stalactite Rain";
            case "stone_bricks_pickaxe" -> "⚡ Fortress Wall";
            case "bricks_pickaxe" -> "⚡ Brick Barrage";
            case "clay_pickaxe" -> "⚡ Mud Trap";
            case "netherrack_pickaxe" -> "⚡ Nether Trail";
            case "basalt_pickaxe" -> "⚡ Basalt Pillar";
            case "blackstone_pickaxe" -> "⚡ Dark Pulse";
            case "crimson_pickaxe" -> "⚡ Blood Rage";
            case "nether_bricks_pickaxe" -> "⚡ Blaze Volley";
            case "packed_ice_pickaxe" -> "⚡ Glacier Ram";
            case "blue_ice_pickaxe" -> "⚡ Absolute Zero";
            case "snow_pickaxe" -> "⚡ Blizzard";
            case "mycelium_pickaxe" -> "⚡ Fungal Bloom";
            case "podzol_pickaxe" -> "⚡ Forest Wrath";
            default -> "⚡ Custom Power";
        };
    }
}
