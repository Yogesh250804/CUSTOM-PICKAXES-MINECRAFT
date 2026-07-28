from PIL import Image
import os
import sys

# Ensure UTF-8 output encoding for symbols like ✓
if hasattr(sys.stdout, 'reconfigure'):
    try:
        sys.stdout.reconfigure(encoding='utf-8')
    except Exception:
        pass


def make_block_pickaxe(base_alpha, base, block_texture_path, output_path, head_bbox=(0, 0, 16, 7)):
    block = Image.open(block_texture_path).convert("RGBA")
    w, h = base.size
    block = block.resize((w, h), Image.NEAREST)

    head_mask = Image.new("L", (w, h), 0)
    hb_crop = base_alpha.crop(head_bbox)
    head_mask.paste(hb_crop, head_bbox)

    result = base.copy()
    result.paste(block, (0, 0), head_mask)
    result.save(output_path)


def batch_generate(base_pickaxe_path, blocks_folder, output_folder, head_bbox=(0, 0, 16, 7)):
    base = Image.open(base_pickaxe_path).convert("RGBA")
    base_alpha = base.split()[3]

    os.makedirs(output_folder, exist_ok=True)

    count = 0
    for filename in os.listdir(blocks_folder):
        if not filename.lower().endswith(".png"):
            continue

        block_texture_path = os.path.join(blocks_folder, filename)
        block_name = os.path.splitext(filename)[0]
        output_name = f"{block_name}_pickaxe.png"
        output_path = os.path.join(output_folder, output_name)

        try:
            make_block_pickaxe(base_alpha, base, block_texture_path, output_path, head_bbox)
            count += 1
            try:
                print(f"✓ {output_name}")
            except Exception:
                print(f"[OK] {output_name}")
        except Exception as e:
            try:
                print(f"✗ Failed for {filename}: {e}")
            except Exception:
                print(f"[ERROR] Failed for {filename}: {e}")

    print(f"\nDone! {count} pickaxe textures generated.")


def batch_generate_food(base_pickaxe_path, items_folder, food_files, output_folder, head_bbox=(0, 0, 16, 11)):
    base = Image.open(base_pickaxe_path).convert("RGBA")
    base_alpha = base.split()[3]

    os.makedirs(output_folder, exist_ok=True)

    count = 0
    for filename in food_files:
        texture_path = os.path.join(items_folder, filename)
        if not os.path.exists(texture_path):
            print(f"⚠️ Warning: Missing source texture {filename} in {items_folder}")
            continue

        food_name = os.path.splitext(filename)[0]
        output_name = f"{food_name}_pickaxe.png"
        output_path = os.path.join(output_folder, output_name)

        try:
            make_block_pickaxe(base_alpha, base, texture_path, output_path, head_bbox)
            count += 1
            try:
                print(f"✓ {output_name}")
            except Exception:
                print(f"[OK] {output_name}")
        except Exception as e:
            try:
                print(f"✗ Failed for {filename}: {e}")
            except Exception:
                print(f"[ERROR] Failed for {filename}: {e}")

    print(f"\nDone! {count} food pickaxe textures generated in {output_folder}.")


if __name__ == "__main__":
    FOOD_ITEMS = [
        "golden_apple.png",
        "enchanted_golden_apple.png",
        "apple.png",
        "cooked_beef.png",
        "cooked_porkchop.png",
        "cooked_chicken.png",
        "cooked_mutton.png",
        "cooked_rabbit.png",
        "cooked_cod.png",
        "cooked_salmon.png",
        "bread.png",
        "cake.png",
        "cookie.png",
        "pumpkin_pie.png",
        "melon_slice.png",
        "carrot.png",
        "potato.png",
        "baked_potato.png",
        "beetroot.png",
        "beetroot_soup.png",
        "mushroom_stew.png",
        "rabbit_stew.png",
        "golden_carrot.png",
        "sweet_berries.png",
        "glow_berries.png",
        "honey_bottle.png",
        "chorus_fruit.png"
    ]

    base_pickaxe = r"C:\Users\yogesh meena\Desktop\MODS\fabric-loader-0.19.3-1.21\assets\minecraft\textures\item\iron_pickaxe.png"
    items_dir = r"C:\Users\yogesh meena\Desktop\MODS\fabric-loader-0.19.3-1.21\assets\minecraft\textures\item"
    food_output_dir = r"src\main\resources\assets\ultimatepickaxes\textures\item\generated_food"

    print("Generating food pickaxe textures...")
    batch_generate_food(
        base_pickaxe_path=base_pickaxe,
        items_folder=items_dir,
        food_files=FOOD_ITEMS,
        output_folder=food_output_dir,
        head_bbox=(0, 0, 16, 11)
    )