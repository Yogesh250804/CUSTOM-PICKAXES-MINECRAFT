# Craftable Recipe Audit & Compliance Report

All **827 pickaxes** in Ultimate Pickaxes have been audited to ensure 100% survival mode craftability. Impossible/unobtainable ingredients (such as Monster Spawners, Bedrock, Command Blocks, and Portal blocks) have been replaced with thematic, obtainable survival equivalents.

## Audit Summary
- **Total Pickaxe Definitions**: 827
- **Total Recipe Files**: 827 (100% Coverage)
- **Missing Recipes**: 0
- **Impossible Recipes Fixed**: 15
- **UTF-8 BOM Cleaned**: Cleaned across all JSON files

## Fixed Unobtainable Recipes

| Pickaxe ID | Original Impossible Ingredient | Survival Crafting Replacement |
| :--- | :--- | :--- |
| `spawner_pickaxe` | `minecraft:spawner` | `minecraft:iron_bars` |
| `trial_spawner_pickaxe` | `minecraft:trial_spawner` | `minecraft:chiseled_tuff_bricks` |
| `bedrock_pickaxe` | `minecraft:bedrock` | `minecraft:crying_obsidian` |
| `command_block_pickaxe` | `minecraft:command_block` | `minecraft:redstone_block` |
| `chain_command_block_pickaxe` | `minecraft:chain_command_block` | `minecraft:chain` |
| `repeating_command_block_pickaxe` | `minecraft:repeating_command_block` | `minecraft:repeater` |
| `end_portal_frame_pickaxe` | `minecraft:end_portal_frame` | `minecraft:ender_eye` |
| `nether_portal_pickaxe` | `minecraft:nether_portal` | `minecraft:obsidian` |
| `structure_block_pickaxe` | `minecraft:structure_block` | `minecraft:smooth_stone` |
| `jigsaw_pickaxe` | `minecraft:jigsaw` | `minecraft:comparator` |
| `reinforced_deepslate_pickaxe` | `minecraft:reinforced_deepslate` | `minecraft:deepslate_bricks` |
| `budding_amethyst_pickaxe` | `minecraft:budding_amethyst` | `minecraft:amethyst_block` |
| `dirt_path_pickaxe` | `minecraft:dirt_path` | `minecraft:dirt` |
| `farmland_pickaxe` | `minecraft:farmland` | `minecraft:dirt` |
| `frogspawn_pickaxe` | `minecraft:frogspawn` | `minecraft:slime_ball` |

---

## Crafting Recipe Design Standard
Every pickaxe recipe follows the standard 3x3 shaped crafting grid format:

```
[ B ] [ B ] [ B ]
[   ] [ S ] [   ]   ==>  [ Custom Pickaxe ]
[   ] [ S ] [   ]
```
Where:
- `B` = Survival Block / Item corresponding to the Pickaxe Theme
- `S` = Stick (`minecraft:stick`)
