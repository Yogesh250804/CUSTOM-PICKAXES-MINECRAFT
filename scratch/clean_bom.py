import json
import os

RECIPE_DIR = r"c:\Users\yogesh meena\Desktop\PROJECTS\MINECRAFT , BUT YOU CAN CRAFT PICKAXE OF ANY BLOCK\src\main\resources\data\ultimatepickaxes\recipe"

for r_name in os.listdir(RECIPE_DIR):
    if not r_name.endswith(".json"):
        continue
    filepath = os.path.join(RECIPE_DIR, r_name)
    try:
        with open(filepath, "r", encoding="utf-8-sig") as f:
            data = json.load(f)
        with open(filepath, "w", encoding="utf-8") as f:
            json.dump(data, f, indent=2)
    except Exception as e:
        print(f"Error cleaning {r_name}: {e}")

print("Cleaned BOM headers across all recipe files!")
