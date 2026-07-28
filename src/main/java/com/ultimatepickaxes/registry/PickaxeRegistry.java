package com.ultimatepickaxes.registry;

import com.ultimatepickaxes.items.DynamicToolMaterial;
import com.ultimatepickaxes.items.UltimatePickaxeItem;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PickaxeRegistry {
    public static final Map<String, UltimatePickaxeItem> PICKAXES = new HashMap<>();
    public static final List<ItemStack> TAB_ITEMS = new ArrayList<>();

    public static final RegistryKey<ItemGroup> ITEM_GROUP_KEY = RegistryKey.of(RegistryKeys.ITEM_GROUP, Identifier.of("ultimatepickaxes", "ultimate_pickaxes_group"));
    public static final ItemGroup ITEM_GROUP = FabricItemGroup.builder()
            .icon(() -> new ItemStack(PICKAXES.values().stream().findFirst().map(i -> (Item) i).orElse(Items.DIAMOND_PICKAXE)))
            .displayName(Text.literal("Ultimate Pickaxes"))
            .entries((displayContext, entries) -> {
                for (ItemStack stack : TAB_ITEMS) {
                    entries.add(stack);
                }
            })
            .build();

    public static void init() {
        Registry.register(Registries.ITEM_GROUP, ITEM_GROUP_KEY, ITEM_GROUP);

        List<PickaxeDefinition> definitions = PickaxeJsonLoader.loadAll();
        VerifyAllPickaxes.verifyAll(definitions);
        for (PickaxeDefinition def : definitions) {
            Item ingredientItem = Registries.ITEM.get(Identifier.of(def.getIngredient()));
            Ingredient repairIngredient = ingredientItem != null ? Ingredient.ofItems(ingredientItem) : Ingredient.EMPTY;

            DynamicToolMaterial material = new DynamicToolMaterial(
                def.getDurability(),
                def.getMiningSpeed(),
                def.getAttackDamage(),
                repairIngredient
            );

            Item.Settings settings = new Item.Settings();

            UltimatePickaxeItem item = new UltimatePickaxeItem(def, material, settings);
            Identifier id = Identifier.of("ultimatepickaxes", def.getId());

            Registry.register(Registries.ITEM, id, item);
            PICKAXES.put(def.getId(), item);
            TAB_ITEMS.add(new ItemStack(item));
        }
    }
}
