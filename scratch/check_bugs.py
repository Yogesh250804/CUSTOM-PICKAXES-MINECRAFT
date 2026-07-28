import json
import os
import re

BASE_DIR = r"c:\Users\yogesh meena\Desktop\PROJECTS\MINECRAFT , BUT YOU CAN CRAFT PICKAXE OF ANY BLOCK"
PICKAXE_DIR = os.path.join(BASE_DIR, r"src\main\resources\data\ultimatepickaxes\pickaxes")
ABILITY_REGISTRY_FILE = os.path.join(BASE_DIR, r"src\main\java\com\ultimatepickaxes\engine\ability\AbilityRegistry.java")
COMPONENTS_DIR = os.path.join(BASE_DIR, r"src\main\java\com\ultimatepickaxes\engine\ability\components")
TEXTURES_ITEM_DIR = os.path.join(BASE_DIR, r"src\main\resources\assets\ultimatepickaxes\textures\item")
TEXTURES_GEN_DIR = os.path.join(BASE_DIR, r"src\main\resources\assets\ultimatepickaxes\textures\item\generated")
TEXTURES_GEN_FOOD_DIR = os.path.join(BASE_DIR, r"src\main\resources\assets\ultimatepickaxes\textures\item\generated_food")

# 1. Parse registered ability IDs from AbilityRegistry.java
registered_ids = set()
with open(ABILITY_REGISTRY_FILE, "r", encoding="utf-8") as f:
    content = f.read()

for match in re.finditer(r'(?:register|reg)\(\s*"([^"]+)"', content):
    registered_ids.add(match.group(1).lower())

print("=== REGISTERED ABILITIES IN AbilityRegistry.init() ===")
print(f"Total registered abilities: {len(registered_ids)}")

# 2. Collect triggers from pickaxe JSON files
pickaxe_abilities = {} # ability_type -> list of pickaxe_ids
all_pickaxe_ids = set()

for filename in os.listdir(PICKAXE_DIR):
    if not filename.endswith(".json"):
        continue
    filepath = os.path.join(PICKAXE_DIR, filename)
    with open(filepath, "r", encoding="utf-8") as f:
        try:
            data = json.load(f)
            p_id = data.get("id", os.path.splitext(filename)[0])
            all_pickaxe_ids.add(p_id)
            triggers = data.get("triggers", {})
            for trigger_name, actions in triggers.items():
                if isinstance(actions, list):
                    for action in actions:
                        if isinstance(action, dict) and "type" in action:
                            ab_type = action["type"].lower()
                            pickaxe_abilities.setdefault(ab_type, []).append(p_id)
        except Exception as e:
            print(f"Error reading {filename}: {e}")

# Find missing abilities
missing_abilities = {}
for ab_type, p_list in pickaxe_abilities.items():
    if ab_type not in registered_ids:
        missing_abilities[ab_type] = p_list

print("\n=== TASK 1: MISSING ABILITY TYPES IN AbilityRegistry.java ===")
if missing_abilities:
    for ab_type, p_list in missing_abilities.items():
        print(f"• '{ab_type}' (used by {len(p_list)} pickaxes): {', '.join(p_list)}")
else:
    print("ALL ABILITY TYPES ARE REGISTERED! Missing types: 0")

# 3. Task 2: Check if Java classes exist in engine/ability/components/
print("\n=== TASK 2: EXISTENCE OF JAVA COMPONENTS FOR MISSING ABILITIES ===")
component_files = set(f.lower() for f in os.listdir(COMPONENTS_DIR) if f.endswith(".java"))

if missing_abilities:
    for ab_type in missing_abilities.keys():
        words = ab_type.split("_")
        pascal_name = "".join(w.capitalize() for w in words) + "Ability.java"
        pascal_lower = pascal_name.lower()
        
        found = False
        matching_file = ""
        for f in component_files:
            if f == pascal_lower or pascal_lower.replace("ability", "") in f:
                found = True
                matching_file = f
                break
                
        if found:
            print(f"• '{ab_type}': CLASS EXISTS ({matching_file}) - Needs register() line in AbilityRegistry.java!")
        else:
            print(f"• '{ab_type}': CLASS MISSING ({pascal_name}) - Needs implementation AND registration!")
else:
    print("NO MISSING ABILITY COMPONENTS!")

# 4. Check Bug 1: Textures directly inside textures/item/
print("\n=== BUG 1: TEXTURE CHECK IN textures/item/ ===")
item_textures = set(f for f in os.listdir(TEXTURES_ITEM_DIR) if f.endswith(".png"))
gen_textures = set(f for f in os.listdir(TEXTURES_GEN_DIR) if f.endswith(".png")) if os.path.exists(TEXTURES_GEN_DIR) else set()
gen_food_textures = set(f for f in os.listdir(TEXTURES_GEN_FOOD_DIR) if f.endswith(".png")) if os.path.exists(TEXTURES_GEN_FOOD_DIR) else set()

missing_direct_textures = []
found_in_gen = []
found_in_gen_food = []
truly_missing = []

for p_id in sorted(all_pickaxe_ids):
    tex_name = f"{p_id}.png"
    if tex_name not in item_textures:
        missing_direct_textures.append(p_id)
        if tex_name in gen_textures:
            found_in_gen.append(p_id)
        elif tex_name in gen_food_textures:
            found_in_gen_food.append(p_id)
        else:
            truly_missing.append(p_id)

print(f"Total pickaxes: {len(all_pickaxe_ids)}")
print(f"Direct textures in textures/item/: {len(item_textures)}")
print(f"Missing direct textures in textures/item/: {len(missing_direct_textures)}")
print(f"  -> Found in generated/: {len(found_in_gen)}")
print(f"  -> Found in generated_food/: {len(found_in_gen_food)}")
print(f"  -> Truly missing from all folders: {len(truly_missing)}")
if truly_missing:
    print(f"  Truly missing list: {truly_missing}")
