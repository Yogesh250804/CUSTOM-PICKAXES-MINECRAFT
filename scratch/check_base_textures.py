import os

BASE_DIR = r"c:\Users\yogesh meena\Desktop\PROJECTS\MINECRAFT , BUT YOU CAN CRAFT PICKAXE OF ANY BLOCK"
TEXTURES_ITEM_DIR = os.path.join(BASE_DIR, r"src\main\resources\assets\ultimatepickaxes\textures\item")
TEXTURES_GEN_DIR = os.path.join(BASE_DIR, r"src\main\resources\assets\ultimatepickaxes\textures\item\generated")

truly_missing = ['amethyst_pickaxe', 'bone_pickaxe', 'coal_pickaxe', 'copper_pickaxe', 'crimson_pickaxe', 'diamond_pickaxe', 'dripstone_pickaxe', 'emerald_pickaxe', 'gold_pickaxe', 'hay_bale_pickaxe', 'honey_pickaxe', 'iron_pickaxe', 'lapis_pickaxe', 'moss_pickaxe', 'netherite_pickaxe', 'nylium_pickaxe', 'purpur_pickaxe', 'quartz_pickaxe', 'redstone_pickaxe', 'shulker_pickaxe', 'slime_pickaxe', 'warped_pickaxe', 'wood_pickaxe', 'wool_pickaxe']

print("=== CHECKING BASE PICKAXES AGAINST EXISTING BLOCK/ITEM TEXTURES ===")
for p in truly_missing:
    # e.g., gold_pickaxe -> gold_block_pickaxe.png or gold_ore_pickaxe.png or iron_pickaxe.png
    base_name = p.replace("_pickaxe", "")
    candidates = [
        f"{base_name}_block_pickaxe.png",
        f"{base_name}_ore_pickaxe.png",
        f"{base_name}_planks_pickaxe.png",
        f"{base_name}_stem_pickaxe.png",
        f"oak_planks_pickaxe.png" if "wood" in base_name else "",
        f"white_wool_pickaxe.png" if "wool" in base_name else "",
        f"hay_block_pickaxe.png" if "hay_bale" in base_name else "",
        f"honey_block_pickaxe.png" if "honey" in base_name else "",
        f"slime_block_pickaxe.png" if "slime" in base_name else "",
        f"moss_block_pickaxe.png" if "moss" in base_name else "",
        f"dripstone_block_pickaxe.png" if "dripstone" in base_name else "",
        f"purpur_block_pickaxe.png" if "purpur" in base_name else "",
        f"bone_block_pickaxe.png" if "bone" in base_name else ""
    ]
    
    found_cand = None
    for cand in candidates:
        if not cand:
            continue
        if os.path.exists(os.path.join(TEXTURES_ITEM_DIR, cand)):
            found_cand = cand
            break
        elif os.path.exists(os.path.join(TEXTURES_GEN_DIR, cand)):
            found_cand = f"generated/{cand}"
            break

    if found_cand:
        print(f"• {p}: Matches candidate texture {found_cand}")
    else:
        print(f"• {p}: NO CANDIDATE FOUND")
