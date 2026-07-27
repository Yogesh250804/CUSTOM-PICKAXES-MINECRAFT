package com.ultimatepickaxes.items;

import com.ultimatepickaxes.engine.ability.AbilityContext;
import com.ultimatepickaxes.engine.cooldown.CooldownManager;
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
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
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
            if (CooldownManager.isOnCooldown(user.getUuid(), definition.getId())) {
                int remaining = CooldownManager.getRemainingTicks(user.getUuid(), definition.getId());
                float remainingSec = remaining / 20.0f;
                user.sendMessage(Text.literal(String.format("§cAbility on Cooldown (%.1fs remaining)", remainingSec)), true);
                world.playSound(null, user.getBlockPos(), SoundEvents.BLOCK_NOTE_BLOCK_BASS.value(), SoundCategory.PLAYERS, 0.5f, 0.5f);
                return TypedActionResult.fail(stack);
            }

            AbilityContext ctx = new AbilityContext(world, user, stack, user.getBlockPos(), null, null, null, TriggerType.ON_RIGHT_CLICK);
            boolean executed = TriggerDispatcher.dispatch(TriggerType.ON_RIGHT_CLICK, ctx);
            if (executed) {
                CooldownManager.setCooldown(user.getUuid(), definition.getId(), definition.getCooldown());
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
    public boolean hasGlint(ItemStack stack) {
        return definition.getRarity().isGlowing() || super.hasGlint(stack);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.literal(definition.getRarity().getDisplayName() + " Pickaxe").formatted(definition.getRarity().getColor(), Formatting.BOLD));

        if (!definition.getTooltip().isEmpty()) {
            tooltip.add(Text.literal(definition.getTooltip()).formatted(Formatting.GRAY));
        }

        tooltip.add(Text.literal(String.format("⚡ Mining Speed: %.1f | Durability: %d", definition.getMiningSpeed(), definition.getDurability())).formatted(Formatting.DARK_AQUA));
        tooltip.add(Text.literal(String.format("⏱ Cooldown: %.1fs", definition.getCooldown() / 20.0f)).formatted(Formatting.GOLD));

        if (definition.getLore() != null && !definition.getLore().isEmpty()) {
            tooltip.add(Text.empty());
            for (String line : definition.getLore()) {
                tooltip.add(Text.literal(line));
            }
        }
    }
}
