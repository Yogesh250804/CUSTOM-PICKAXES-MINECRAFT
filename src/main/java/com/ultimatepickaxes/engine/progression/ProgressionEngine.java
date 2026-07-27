package com.ultimatepickaxes.engine.progression;

import com.google.gson.JsonObject;
import com.ultimatepickaxes.engine.ability.AbilityContext;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.OverlayMessageS2CPacket;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ProgressionEngine {

    public static void processBlockBreak(JsonObject progressionConfig, AbilityContext context) {
        if (progressionConfig == null || context == null || context.getStack() == null || !(context.getPlayer() instanceof ServerPlayerEntity player)) {
            return;
        }

        if (!progressionConfig.has("enabled") || !progressionConfig.get("enabled").getAsBoolean()) {
            return;
        }

        int xpPerBlock = progressionConfig.has("xpPerBlock") ? progressionConfig.get("xpPerBlock").getAsInt() : 1;
        int xpToEvolve = progressionConfig.has("xpToEvolve") ? progressionConfig.get("xpToEvolve").getAsInt() : 500;
        String nextPickaxeId = progressionConfig.has("nextPickaxe") ? progressionConfig.get("nextPickaxe").getAsString() : null;

        ItemStack stack = context.getStack();
        NbtComponent customData = stack.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT);
        NbtCompound nbt = customData.copyNbt();

        PickaxeProgressionData data = PickaxeProgressionData.fromNbt(nbt);
        data.addXp(xpPerBlock);

        if (nextPickaxeId != null && data.getXp() >= xpToEvolve) {
            Item nextItem = Registries.ITEM.get(Identifier.of(nextPickaxeId));
            if (nextItem != null && nextItem != stack.getItem()) {
                ItemStack evolvedStack = new ItemStack(nextItem, 1);
                player.setStackInHand(player.getActiveHand(), evolvedStack);

                player.getWorld().playSound(null, player.getBlockPos(), SoundEvents.UI_TOAST_CHALLENGE_COMPLETE, SoundCategory.PLAYERS, 1.0f, 1.0f);
                player.networkHandler.sendPacket(new OverlayMessageS2CPacket(Text.literal("§6✨ Your Pickaxe Evolved into " + evolvedStack.getName().getString() + "! ✨")));
                return;
            }
        }

        data.writeToNbt(nbt);
        stack.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(nbt));
    }
}
