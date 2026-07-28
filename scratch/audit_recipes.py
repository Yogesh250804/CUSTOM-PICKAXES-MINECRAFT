import json
import os

BASE_DIR = r"c:\Users\yogesh meena\Desktop\PROJECTS\MINECRAFT , BUT YOU CAN CRAFT PICKAXE OF ANY BLOCK"
PICKAXE_DIR = os.path.join(BASE_DIR, r"src\main\resources\data\ultimatepickaxes\pickaxes")
RECIPE_DIR = os.path.join(BASE_DIR, r"src\main\resources\data\ultimatepickaxes\recipe")

# List of unobtainable survival items/blocks that shouldn't be required in crafting recipes
UNOBTAINABLE_ITEMS = {
    "minecraft:spawner",
    "minecraft:trial_spawner",
    "minecraft:bedrock",
    "minecraft:command_block",
    "minecraft:chain_command_block",
    "minecraft:repeating_command_block",
    "minecraft:barrier",
    "minecraft:light",
    "minecraft:structure_block",
    "minecraft:structure_void",
    "minecraft:jigsaw",
    "minecraft:end_portal_frame",
    "minecraft:end_portal",
    "minecraft:nether_portal",
    "minecraft:reinforced_deepslate",
    "minecraft:frogspawn",
    "minecraft:budding_amethyst",
    "minecraft:farmland",
    "minecraft:dirt_path",
    "minecraft:petrified_oak_slab",
    "minecraft:air",
    "minecraft:cave_air",
    "minecraft:void_air"
}

# Replacement map for unobtainable items to obtainable survival equivalents
REPLACEMENT_MAP = {
    "minecraft:spawner": "minecraft:iron_bars",
    "minecraft:trial_spawner": "minecraft:heavy_core",
    "minecraft:bedrock": "minecraft:crying_obsidian",
    "minecraft:command_block": "minecraft:redstone_block",
    "minecraft:chain_command_block": "minecraft:chain",
    "minecraft:repeating_command_block": "minecraft:repeater",
    "minecraft:barrier": "minecraft:tinted_glass",
    "minecraft:light": "minecraft:glowstone",
    "minecraft:structure_block": "minecraft:smooth_stone",
    "minecraft:structure_void": "minecraft:glass",
    "minecraft:jigsaw": "minecraft:comparator",
    "minecraft:end_portal_frame": "minecraft:ender_eye",
    "minecraft:end_portal": "minecraft:end_stone",
    "minecraft:nether_portal": "minecraft:obsidian",
    "minecraft:reinforced_deepslate": "minecraft:deepslate_bricks",
    "minecraft:frogspawn": "minecraft:slime_ball",
    "minecraft:budding_amethyst": "minecraft:amethyst_block",
    "minecraft:farmland": "minecraft:dirt",
    "minecraft:dirt_path": "minecraft:dirt",
    "minecraft:petrified_oak_slab": "minecraft:oak_slab"
}

all_pickaxes = set()
for f in os.listdir(PICKAXE_DIR):
    if f.endswith(".json"):
        all_pickaxes.add(f.replace(".json", ""))

recipe_files = set()
if os.path.exists(RECIPE_DIR):
    for f in os.listdir(RECIPE_DIR):
        if f.endswith(".json"):
            recipe_files.add(f.replace(".json", ""))

missing_recipes = sorted(list(all_pickaxes - recipe_files))

impossible_recipes = {} # pickaxe_id -> list of impossible ingredients used

for r_name in os.listdir(RECIPE_DIR):
    if not r_name.endswith(".json"):
        continue
    filepath = os.path.join(RECIPE_DIR, r_name)
    p_id = r_name.replace(".json", "")
    try:
        with open(filepath, "r", encoding="utf-8") as f:
            data = json.load(f)
            key_map = data.get("key", {})
            bad_ingredients = []
            for k, item_def in key_map.items():
                item_id = ""
                if isinstance(item_def, dict):
                    item_id = item_def.get("item", "")
                elif isinstance(item_def, str):
                    item_id = item_def
                if item_id in UNOBTAINABLE_ITEMS:
                    bad_ingredients.append(item_id)
            if bad_ingredients:
                impossible_recipes[p_id] = bad_ingredients
    except Exception as e:
        print(f"Error reading recipe {r_name}: {e}")

print("=== RECIPE AUDIT SUMMARY ===")
print(f"Total Pickaxe Definitions: {len(all_pickaxes)}")
print(f"Total Recipe Files: {len(recipe_files)}")
print(f"Missing Recipes: {len(missing_recipes)}")
if missing_recipes:
    print(f"Missing Recipes List: {missing_recipes}")

print(f"\nRecipes using Impossible/Unobtainable Items: {len(impossible_recipes)}")
for p_id, bad_list in impossible_recipes.items():
    print(f"• '{p_id}': uses {bad_list}")
