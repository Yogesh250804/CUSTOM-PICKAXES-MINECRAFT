from PIL import Image
import os

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
            print(f"✓ {output_name}")
        except Exception as e:
            print(f"✗ Failed for {filename}: {e}")

    print(f"\nDone! {count} pickaxe textures generated.")


batch_generate(
    base_pickaxe_path=r"C:\Users\yogesh meena\Desktop\MODS\fabric-loader-0.19.3-1.21\assets\minecraft\textures\item\iron_pickaxe.png",
    blocks_folder=r"C:\Users\yogesh meena\Desktop\MODS\fabric-loader-0.19.3-1.21\assets\minecraft\textures\block",
    output_folder=r"src\main\resources\assets\ultimatepickaxes\textures\item\generated",
    head_bbox=(0, 0, 16, 11)
)