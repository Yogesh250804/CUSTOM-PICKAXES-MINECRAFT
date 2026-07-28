import os
import shutil

BASE_DIR = r"c:\Users\yogesh meena\Desktop\PROJECTS\MINECRAFT , BUT YOU CAN CRAFT PICKAXE OF ANY BLOCK"
TEXTURES_ITEM_DIR = os.path.join(BASE_DIR, r"src\main\resources\assets\ultimatepickaxes\textures\item")
TEXTURES_GEN_FOOD_DIR = os.path.join(BASE_DIR, r"src\main\resources\assets\ultimatepickaxes\textures\item\generated_food")

# 1. Copy 24 food pickaxe textures from generated_food/ to textures/item/
food_files = [f for f in os.listdir(TEXTURES_GEN_FOOD_DIR) if f.endswith(".png")]
copied_food = 0
for f in food_files:
    src = os.path.join(TEXTURES_GEN_FOOD_DIR, f)
    dst = os.path.join(TEXTURES_ITEM_DIR, f)
    if not os.path.exists(dst):
        shutil.copy2(src, dst)
        copied_food += 1

print(f"Copied {copied_food} food textures into textures/item/")

# 2. Map 24 base pickaxes to their existing block pickaxe textures in textures/item/
base_mappings = {
    'amethyst_pickaxe': 'amethyst_block_pickaxe.png',
    'bone_pickaxe': 'bone_block_pickaxe.png',
    'coal_pickaxe': 'coal_block_pickaxe.png',
    'copper_pickaxe': 'copper_block_pickaxe.png',
    'crimson_pickaxe': 'crimson_planks_pickaxe.png',
    'diamond_pickaxe': 'diamond_block_pickaxe.png',
    'dripstone_pickaxe': 'dripstone_block_pickaxe.png',
    'emerald_pickaxe': 'emerald_block_pickaxe.png',
    'gold_pickaxe': 'gold_block_pickaxe.png',
    'hay_bale_pickaxe': 'hay_block_pickaxe.png',
    'honey_pickaxe': 'honey_block_pickaxe.png',
    'iron_pickaxe': 'iron_block_pickaxe.png',
    'lapis_pickaxe': 'lapis_block_pickaxe.png',
    'moss_pickaxe': 'moss_block_pickaxe.png',
    'netherite_pickaxe': 'netherite_block_pickaxe.png',
    'nylium_pickaxe': 'crimson_nylium_pickaxe.png',
    'purpur_pickaxe': 'purpur_block_pickaxe.png',
    'quartz_pickaxe': 'quartz_block_pickaxe.png',
    'redstone_pickaxe': 'redstone_block_pickaxe.png',
    'shulker_pickaxe': 'shulker_box_pickaxe.png',
    'slime_pickaxe': 'slime_block_pickaxe.png',
    'warped_pickaxe': 'warped_planks_pickaxe.png',
    'wood_pickaxe': 'oak_planks_pickaxe.png',
    'wool_pickaxe': 'white_wool_pickaxe.png'
}

copied_base = 0
for base_p, src_tex in base_mappings.items():
    dst = os.path.join(TEXTURES_ITEM_DIR, f"{base_p}.png")
    src = os.path.join(TEXTURES_ITEM_DIR, src_tex)
    if os.path.exists(src) and not os.path.exists(dst):
        shutil.copy2(src, dst)
        copied_base += 1
    elif not os.path.exists(src):
        print(f"Warning: Source {src_tex} for {base_p} not found!")

print(f"Copied {copied_base} base pickaxe textures into textures/item/")
