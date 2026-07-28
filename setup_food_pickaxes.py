import json
import os

FOOD_PICKAXES = [
    {
        "id": "golden_apple_pickaxe",
        "displayName": "Golden Apple Pickaxe",
        "ingredient": "minecraft:golden_apple",
        "durability": 1500,
        "miningSpeed": 10.0,
        "attackDamage": 7.0,
        "rarity": "EPIC",
        "cooldown": 180,
        "tooltip": "Golden Overflow! Converts terrain into Gold Blocks, heals fully, and paralyzes enemies.",
        "triggers": {
            "ON_RIGHT_CLICK": [
                {
                    "type": "golden_overflow",
                    "params": {"radius": 4}
                }
            ]
        }
    },
    {
        "id": "apple_pickaxe",
        "displayName": "Apple Pickaxe",
        "ingredient": "minecraft:apple",
        "durability": 300,
        "miningSpeed": 6.0,
        "attackDamage": 4.0,
        "rarity": "UNCOMMON",
        "cooldown": 80,
        "tooltip": "Gravity Apple Drop! Summons giant crushing apples from the sky onto enemies.",
        "triggers": {
            "ON_RIGHT_CLICK": [
                {
                    "type": "gravity_apple_drop",
                    "params": {}
                }
            ]
        }
    },
    {
        "id": "cooked_beef_pickaxe",
        "displayName": "Steak Pickaxe",
        "ingredient": "minecraft:cooked_beef",
        "durability": 600,
        "miningSpeed": 7.0,
        "attackDamage": 5.5,
        "rarity": "RARE",
        "cooldown": 120,
        "tooltip": "Carnivore Rage! Predator roar that ignites, cooks, and launches surrounding enemies.",
        "triggers": {
            "ON_RIGHT_CLICK": [
                {
                    "type": "carnivore_rage",
                    "params": {}
                }
            ]
        }
    },
    {
        "id": "cooked_porkchop_pickaxe",
        "displayName": "Cooked Porkchop Pickaxe",
        "ingredient": "minecraft:cooked_porkchop",
        "durability": 600,
        "miningSpeed": 7.0,
        "attackDamage": 5.5,
        "rarity": "RARE",
        "cooldown": 120,
        "tooltip": "Carnivore Rage! Unleashes a flaming shockwave roar.",
        "triggers": {
            "ON_RIGHT_CLICK": [
                {
                    "type": "carnivore_rage",
                    "params": {}
                }
            ]
        }
    },
    {
        "id": "cooked_chicken_pickaxe",
        "displayName": "Cooked Chicken Pickaxe",
        "ingredient": "minecraft:cooked_chicken",
        "durability": 500,
        "miningSpeed": 6.5,
        "attackDamage": 4.5,
        "rarity": "UNCOMMON",
        "cooldown": 100,
        "tooltip": "Carnivore Rage! Primal roar with speed and flight burst.",
        "triggers": {
            "ON_RIGHT_CLICK": [
                {
                    "type": "carnivore_rage",
                    "params": {}
                }
            ]
        }
    },
    {
        "id": "cooked_mutton_pickaxe",
        "displayName": "Cooked Mutton Pickaxe",
        "ingredient": "minecraft:cooked_mutton",
        "durability": 550,
        "miningSpeed": 6.5,
        "attackDamage": 5.0,
        "rarity": "UNCOMMON",
        "cooldown": 100,
        "tooltip": "Carnivore Rage! High-impact predator roar.",
        "triggers": {
            "ON_RIGHT_CLICK": [
                {
                    "type": "carnivore_rage",
                    "params": {}
                }
            ]
        }
    },
    {
        "id": "cooked_rabbit_pickaxe",
        "displayName": "Cooked Rabbit Pickaxe",
        "ingredient": "minecraft:cooked_rabbit",
        "durability": 500,
        "miningSpeed": 7.0,
        "attackDamage": 4.5,
        "rarity": "UNCOMMON",
        "cooldown": 100,
        "tooltip": "Carnivore Rage! Agile predator shockwave.",
        "triggers": {
            "ON_RIGHT_CLICK": [
                {
                    "type": "carnivore_rage",
                    "params": {}
                }
            ]
        }
    },
    {
        "id": "cooked_cod_pickaxe",
        "displayName": "Cooked Cod Pickaxe",
        "ingredient": "minecraft:cooked_cod",
        "durability": 550,
        "miningSpeed": 6.5,
        "attackDamage": 4.5,
        "rarity": "UNCOMMON",
        "cooldown": 100,
        "tooltip": "Tidal Wave! Summons a crashing aquatic wave.",
        "triggers": {
            "ON_RIGHT_CLICK": [
                {
                    "type": "tidal_wave",
                    "params": {}
                }
            ]
        }
    },
    {
        "id": "cooked_salmon_pickaxe",
        "displayName": "Cooked Salmon Pickaxe",
        "ingredient": "minecraft:cooked_salmon",
        "durability": 550,
        "miningSpeed": 7.0,
        "attackDamage": 5.0,
        "rarity": "UNCOMMON",
        "cooldown": 100,
        "tooltip": "Tidal Wave! Sweeps enemies away in a water torrent.",
        "triggers": {
            "ON_RIGHT_CLICK": [
                {
                    "type": "tidal_wave",
                    "params": {}
                }
            ]
        }
    },
    {
        "id": "bread_pickaxe",
        "displayName": "Bread Pickaxe",
        "ingredient": "minecraft:bread",
        "durability": 400,
        "miningSpeed": 6.0,
        "attackDamage": 4.0,
        "rarity": "COMMON",
        "cooldown": 80,
        "tooltip": "Harvest Wave! Cleanses area and gives swift haste burst.",
        "triggers": {
            "ON_RIGHT_CLICK": [
                {
                    "type": "food_feast",
                    "params": {
                        "hunger": 20,
                        "saturation": 1.0,
                        "effects": [
                            {"id": "minecraft:haste", "duration": 400, "amplifier": 3},
                            {"id": "minecraft:speed", "duration": 400, "amplifier": 2}
                        ]
                    }
                }
            ]
        }
    },
    {
        "id": "cake_pickaxe",
        "displayName": "Cake Pickaxe",
        "ingredient": "minecraft:cake",
        "durability": 800,
        "miningSpeed": 8.0,
        "attackDamage": 5.0,
        "rarity": "RARE",
        "cooldown": 100,
        "tooltip": "Cake Explosion! Fires fireworks and encases mobs in sticky cake frosting.",
        "triggers": {
            "ON_RIGHT_CLICK": [
                {
                    "type": "cake_explosion",
                    "params": {}
                }
            ]
        }
    },
    {
        "id": "cookie_pickaxe",
        "displayName": "Cookie Pickaxe",
        "ingredient": "minecraft:cookie",
        "durability": 350,
        "miningSpeed": 6.0,
        "attackDamage": 3.5,
        "rarity": "COMMON",
        "cooldown": 60,
        "tooltip": "Cookie Frenzy! Rapid gatling barrage of exploding cookie shrapnel.",
        "triggers": {
            "ON_RIGHT_CLICK": [
                {
                    "type": "cookie_frenzy",
                    "params": {}
                }
            ]
        }
    },
    {
        "id": "pumpkin_pie_pickaxe",
        "displayName": "Pumpkin Pie Pickaxe",
        "ingredient": "minecraft:pumpkin_pie",
        "durability": 600,
        "miningSpeed": 7.0,
        "attackDamage": 4.5,
        "rarity": "RARE",
        "cooldown": 120,
        "tooltip": "Pumpkin Curse! Forces glowing Jack-O-Lanterns onto enemy heads with fire.",
        "triggers": {
            "ON_RIGHT_CLICK": [
                {
                    "type": "pumpkin_curse",
                    "params": {}
                }
            ]
        }
    },
    {
        "id": "melon_slice_pickaxe",
        "displayName": "Melon Slice Pickaxe",
        "ingredient": "minecraft:melon_slice",
        "durability": 350,
        "miningSpeed": 6.0,
        "attackDamage": 3.5,
        "rarity": "COMMON",
        "cooldown": 60,
        "tooltip": "Melon Cannonade! Rapid machine-gun melon seed explosion.",
        "triggers": {
            "ON_RIGHT_CLICK": [
                {
                    "type": "melon_cannonade",
                    "params": {}
                }
            ]
        }
    },
    {
        "id": "carrot_pickaxe",
        "displayName": "Carrot Pickaxe",
        "ingredient": "minecraft:carrot",
        "durability": 350,
        "miningSpeed": 6.0,
        "attackDamage": 3.5,
        "rarity": "COMMON",
        "cooldown": 60,
        "tooltip": "Golden Laser Beam! Fires piercing laser beam through blocks and mobs.",
        "triggers": {
            "ON_RIGHT_CLICK": [
                {
                    "type": "golden_laser",
                    "params": {"range": 25.0}
                }
            ]
        }
    },
    {
        "id": "potato_pickaxe",
        "displayName": "Potato Pickaxe",
        "ingredient": "minecraft:potato",
        "durability": 300,
        "miningSpeed": 5.5,
        "attackDamage": 3.5,
        "rarity": "COMMON",
        "cooldown": 60,
        "tooltip": "Gravity Spud Drop! Launches heavy crushing potato strikes.",
        "triggers": {
            "ON_RIGHT_CLICK": [
                {
                    "type": "gravity_apple_drop",
                    "params": {}
                }
            ]
        }
    },
    {
        "id": "baked_potato_pickaxe",
        "displayName": "Baked Potato Pickaxe",
        "ingredient": "minecraft:baked_potato",
        "durability": 450,
        "miningSpeed": 6.5,
        "attackDamage": 4.0,
        "rarity": "COMMON",
        "cooldown": 80,
        "tooltip": "Volcanic Eruption! Erupts scorching fiery magma rock around player.",
        "triggers": {
            "ON_RIGHT_CLICK": [
                {
                    "type": "volcanic_eruption",
                    "params": {}
                }
            ]
        }
    },
    {
        "id": "beetroot_pickaxe",
        "displayName": "Beetroot Pickaxe",
        "ingredient": "minecraft:beetroot",
        "durability": 350,
        "miningSpeed": 6.0,
        "attackDamage": 3.5,
        "rarity": "COMMON",
        "cooldown": 80,
        "tooltip": "Supernova Illumination! Spawns glowing light sun that burns undead.",
        "triggers": {
            "ON_RIGHT_CLICK": [
                {
                    "type": "supernova_illumination",
                    "params": {}
                }
            ]
        }
    },
    {
        "id": "beetroot_soup_pickaxe",
        "displayName": "Beetroot Soup Pickaxe",
        "ingredient": "minecraft:beetroot_soup",
        "durability": 550,
        "miningSpeed": 7.0,
        "attackDamage": 4.5,
        "rarity": "RARE",
        "cooldown": 100,
        "tooltip": "All Buffs Overflow! Grants every positive status effect at once.",
        "triggers": {
            "ON_RIGHT_CLICK": [
                {
                    "type": "all_buffs",
                    "params": {}
                }
            ]
        }
    },
    {
        "id": "mushroom_stew_pickaxe",
        "displayName": "Mushroom Stew Pickaxe",
        "ingredient": "minecraft:mushroom_stew",
        "durability": 550,
        "miningSpeed": 7.0,
        "attackDamage": 4.5,
        "rarity": "RARE",
        "cooldown": 100,
        "tooltip": "Fungal Bloom! Spawns explosive spore clouds that heal allies and poison enemies.",
        "triggers": {
            "ON_RIGHT_CLICK": [
                {
                    "type": "fungal_bloom",
                    "params": {}
                }
            ]
        }
    },
    {
        "id": "rabbit_stew_pickaxe",
        "displayName": "Rabbit Stew Pickaxe",
        "ingredient": "minecraft:rabbit_stew",
        "durability": 800,
        "miningSpeed": 8.5,
        "attackDamage": 5.5,
        "rarity": "RARE",
        "cooldown": 100,
        "tooltip": "All Buffs Overflow! Grants supreme stats, speed, jump, and luck.",
        "triggers": {
            "ON_RIGHT_CLICK": [
                {
                    "type": "all_buffs",
                    "params": {}
                }
            ]
        }
    },
    {
        "id": "golden_carrot_pickaxe",
        "displayName": "Golden Carrot Pickaxe",
        "ingredient": "minecraft:golden_carrot",
        "durability": 1400,
        "miningSpeed": 9.5,
        "attackDamage": 6.5,
        "rarity": "EPIC",
        "cooldown": 120,
        "tooltip": "Golden Solar Laser! Piercing eye laser beam that reveals X-Ray glowing entities.",
        "triggers": {
            "ON_RIGHT_CLICK": [
                {
                    "type": "golden_laser",
                    "params": {"range": 40.0}
                }
            ]
        }
    },
    {
        "id": "sweet_berries_pickaxe",
        "displayName": "Sweet Berries Pickaxe",
        "ingredient": "minecraft:sweet_berries",
        "durability": 350,
        "miningSpeed": 6.0,
        "attackDamage": 3.5,
        "rarity": "COMMON",
        "cooldown": 80,
        "tooltip": "Thorny Snare! Traps surrounding enemies in prickly thorn roots.",
        "triggers": {
            "ON_RIGHT_CLICK": [
                {
                    "type": "root_snare",
                    "params": {}
                }
            ]
        }
    },
    {
        "id": "glow_berries_pickaxe",
        "displayName": "Glow Berries Pickaxe",
        "ingredient": "minecraft:glow_berries",
        "durability": 450,
        "miningSpeed": 6.5,
        "attackDamage": 4.0,
        "rarity": "UNCOMMON",
        "cooldown": 100,
        "tooltip": "Supernova Illumination! Mini glowing sun that smites undead and grants X-Ray vision.",
        "triggers": {
            "ON_RIGHT_CLICK": [
                {
                    "type": "supernova_illumination",
                    "params": {}
                }
            ]
        }
    },
    {
        "id": "honey_bottle_pickaxe",
        "displayName": "Honey Bottle Pickaxe",
        "ingredient": "minecraft:honey_bottle",
        "durability": 900,
        "miningSpeed": 8.0,
        "attackDamage": 5.0,
        "rarity": "RARE",
        "cooldown": 120,
        "tooltip": "Sticky Honey Trap! Creates a honey pool that immobilizes enemies and drains health.",
        "triggers": {
            "ON_RIGHT_CLICK": [
                {
                    "type": "sticky_honey_trap",
                    "params": {}
                }
            ]
        }
    },
    {
        "id": "chorus_fruit_pickaxe",
        "displayName": "Chorus Fruit Pickaxe",
        "ingredient": "minecraft:chorus_fruit",
        "durability": 1500,
        "miningSpeed": 10.0,
        "attackDamage": 6.5,
        "rarity": "EPIC",
        "cooldown": 30,
        "tooltip": "Dimensional Void Warp! Opens a void rift pulling enemies in while teleporting player.",
        "triggers": {
            "ON_RIGHT_CLICK": [
                {
                    "type": "dimensional_warp",
                    "params": {}
                }
            ]
        }
    }
]

BASE_DIR = r"c:\Users\yogesh meena\Desktop\PROJECTS\MINECRAFT , BUT YOU CAN CRAFT PICKAXE OF ANY BLOCK"
PICKAXE_DATA_DIR = os.path.join(BASE_DIR, r"src\main\resources\data\ultimatepickaxes\pickaxes")
RECIPE_DIR = os.path.join(BASE_DIR, r"src\main\resources\data\ultimatepickaxes\recipe")
MODEL_DIR = os.path.join(BASE_DIR, r"src\main\resources\assets\ultimatepickaxes\models\item")
LANG_FILE = os.path.join(BASE_DIR, r"src\main\resources\assets\ultimatepickaxes\lang\en_us.json")

os.makedirs(PICKAXE_DATA_DIR, exist_ok=True)
os.makedirs(RECIPE_DIR, exist_ok=True)
os.makedirs(MODEL_DIR, exist_ok=True)

with open(LANG_FILE, "r", encoding="utf-8") as f:
    lang_data = json.load(f)

for item in FOOD_PICKAXES:
    item_id = item["id"]
    display_name = item["displayName"]
    ingredient = item["ingredient"]

    # 1. Pickaxe JSON Definition
    pickaxe_json_path = os.path.join(PICKAXE_DATA_DIR, f"{item_id}.json")
    with open(pickaxe_json_path, "w", encoding="utf-8") as f:
        json.dump(item, f, indent=4)

    # 2. Recipe JSON Definition
    recipe_json = {
        "type": "minecraft:crafting_shaped",
        "category": "equipment",
        "pattern": [
            "###",
            " S ",
            " S "
        ],
        "key": {
            "#": {
                "item": ingredient
            },
            "S": {
                "item": "minecraft:stick"
            }
        },
        "result": {
            "id": f"ultimatepickaxes:{item_id}",
            "count": 1
        }
    }
    recipe_json_path = os.path.join(RECIPE_DIR, f"{item_id}.json")
    with open(recipe_json_path, "w", encoding="utf-8") as f:
        json.dump(recipe_json, f, indent=2)

    # 3. Model JSON Definition
    model_json = {
        "parent": "minecraft:item/handheld",
        "textures": {
            "layer0": f"ultimatepickaxes:item/generated_food/{item_id}"
        }
    }
    model_json_path = os.path.join(MODEL_DIR, f"{item_id}.json")
    with open(model_json_path, "w", encoding="utf-8") as f:
        json.dump(model_json, f, indent=2)

    # 4. Lang key
    lang_key = f"item.ultimatepickaxes.{item_id}"
    lang_data[lang_key] = display_name

with open(LANG_FILE, "w", encoding="utf-8") as f:
    json.dump(lang_data, f, indent=2, ensure_ascii=False)

print(f"Successfully updated {len(FOOD_PICKAXES)} food pickaxes with wild custom non-vanilla abilities!")
