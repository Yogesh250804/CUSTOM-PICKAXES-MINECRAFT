import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

public class TextureGenerator {

    public static void main(String[] args) throws Exception {
        File modelsDir = new File("src/main/resources/assets/ultimatepickaxes/models/item");
        File texturesDir = new File("src/main/resources/assets/ultimatepickaxes/textures/item");
        modelsDir.mkdirs();
        texturesDir.mkdirs();

        Map<String, Color[]> pickaxeColors = new HashMap<>();
        pickaxeColors.put("dirt_pickaxe", new Color[]{new Color(134, 96, 67), new Color(87, 61, 38), new Color(175, 128, 91)});
        pickaxeColors.put("cobblestone_pickaxe", new Color[]{new Color(120, 120, 120), new Color(80, 80, 80), new Color(160, 160, 160)});
        pickaxeColors.put("tnt_pickaxe", new Color[]{new Color(219, 57, 43), new Color(168, 36, 24), new Color(245, 245, 245)});
        pickaxeColors.put("glass_pickaxe", new Color[]{new Color(188, 231, 236, 200), new Color(128, 194, 204, 180), new Color(232, 250, 252, 240)});
        pickaxeColors.put("sponge_pickaxe", new Color[]{new Color(194, 184, 54), new Color(140, 133, 35), new Color(225, 215, 80)});
        pickaxeColors.put("honey_pickaxe", new Color[]{new Color(237, 157, 27, 230), new Color(180, 110, 15, 210), new Color(247, 194, 94, 250)});
        pickaxeColors.put("magma_pickaxe", new Color[]{new Color(232, 85, 23), new Color(59, 19, 14), new Color(247, 148, 29)});
        pickaxeColors.put("slime_pickaxe", new Color[]{new Color(89, 179, 74, 230), new Color(50, 120, 40, 210), new Color(133, 224, 119, 250)});
        pickaxeColors.put("coal_pickaxe", new Color[]{new Color(38, 38, 38), new Color(18, 18, 18), new Color(70, 70, 70)});
        pickaxeColors.put("diamond_pickaxe", new Color[]{new Color(44, 224, 212), new Color(20, 145, 136), new Color(150, 255, 245)});
        pickaxeColors.put("dragon_egg_pickaxe", new Color[]{new Color(43, 12, 56), new Color(18, 3, 24), new Color(178, 61, 232)});
        pickaxeColors.put("amethyst_pickaxe", new Color[]{new Color(154, 92, 198), new Color(106, 55, 143), new Color(198, 140, 230)});
        pickaxeColors.put("redstone_pickaxe", new Color[]{new Color(230, 21, 21), new Color(143, 11, 11), new Color(255, 90, 90)});
        pickaxeColors.put("gold_pickaxe", new Color[]{new Color(245, 206, 91), new Color(180, 140, 40), new Color(255, 235, 150)});
        pickaxeColors.put("iron_pickaxe", new Color[]{new Color(210, 210, 210), new Color(150, 150, 150), new Color(245, 245, 245)});
        pickaxeColors.put("obsidian_pickaxe", new Color[]{new Color(30, 22, 43), new Color(15, 10, 23), new Color(75, 55, 105)});
        pickaxeColors.put("glowstone_pickaxe", new Color[]{new Color(245, 206, 91), new Color(190, 140, 40), new Color(250, 230, 150)});
        pickaxeColors.put("netherite_pickaxe", new Color[]{new Color(68, 58, 59), new Color(40, 34, 35), new Color(105, 92, 93)});
        pickaxeColors.put("apple_pickaxe", new Color[]{new Color(212, 36, 36), new Color(130, 15, 15), new Color(255, 90, 90)});
        pickaxeColors.put("pumpkin_pickaxe", new Color[]{new Color(214, 107, 21), new Color(140, 60, 10), new Color(245, 150, 60)});
        pickaxeColors.put("cake_pickaxe", new Color[]{new Color(247, 247, 247), new Color(214, 32, 32), new Color(143, 83, 43)});
        pickaxeColors.put("sand_pickaxe", new Color[]{new Color(222, 211, 151), new Color(173, 160, 100), new Color(245, 238, 190)});
        pickaxeColors.put("gravel_pickaxe", new Color[]{new Color(136, 126, 126), new Color(90, 80, 80), new Color(180, 170, 170)});
        pickaxeColors.put("sculk_pickaxe", new Color[]{new Color(11, 46, 56), new Color(0, 121, 140), new Color(0, 168, 150)});
        pickaxeColors.put("wood_pickaxe", new Color[]{new Color(143, 99, 56), new Color(94, 62, 33), new Color(180, 130, 80)});
        pickaxeColors.put("lapis_pickaxe", new Color[]{new Color(38, 77, 180), new Color(20, 45, 120), new Color(85, 130, 230)});
        pickaxeColors.put("emerald_pickaxe", new Color[]{new Color(23, 219, 105), new Color(12, 140, 65), new Color(120, 255, 175)});
        pickaxeColors.put("prismarine_pickaxe", new Color[]{new Color(88, 163, 151), new Color(45, 100, 92), new Color(140, 210, 198)});
        pickaxeColors.put("end_stone_pickaxe", new Color[]{new Color(222, 224, 162), new Color(170, 172, 110), new Color(245, 247, 195)});
        pickaxeColors.put("shulker_pickaxe", new Color[]{new Color(151, 103, 153), new Color(95, 60, 97), new Color(195, 145, 198)});

        for (Map.Entry<String, Color[]> entry : pickaxeColors.entrySet()) {
            String name = entry.getKey();
            Color primary = entry.getValue()[0];
            Color shadow = entry.getValue()[1];
            Color highlight = entry.getValue()[2];

            // 1. Generate 16x16 PNG Texture
            BufferedImage img = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = img.createGraphics();

            // Handle Stick (Diagonal bottom-left to center)
            Color stickBase = new Color(110, 75, 42);
            Color stickDark = new Color(70, 45, 22);

            drawPixel(g, 1, 14, stickDark);
            drawPixel(g, 2, 13, stickBase);
            drawPixel(g, 3, 12, stickBase);
            drawPixel(g, 4, 11, stickBase);
            drawPixel(g, 5, 10, stickBase);
            drawPixel(g, 6, 9, stickBase);
            drawPixel(g, 7, 8, stickBase);
            drawPixel(g, 8, 7, stickBase);
            drawPixel(g, 9, 6, stickDark);

            // Pickaxe Head / Blade
            // Left Blade Tip
            drawPixel(g, 2, 8, shadow);
            drawPixel(g, 3, 9, primary);
            drawPixel(g, 4, 10, primary);

            // Top Arch
            drawPixel(g, 5, 11, highlight);
            drawPixel(g, 6, 12, highlight);
            drawPixel(g, 7, 13, highlight);
            drawPixel(g, 8, 13, highlight);
            drawPixel(g, 9, 13, highlight);
            drawPixel(g, 10, 13, highlight);
            drawPixel(g, 11, 13, highlight);
            drawPixel(g, 12, 12, highlight);

            // Inner Fill
            drawPixel(g, 5, 10, primary);
            drawPixel(g, 6, 11, primary);
            drawPixel(g, 7, 12, primary);
            drawPixel(g, 8, 12, primary);
            drawPixel(g, 9, 12, primary);
            drawPixel(g, 10, 12, primary);
            drawPixel(g, 11, 12, primary);
            drawPixel(g, 12, 11, primary);

            // Right Blade Tip
            drawPixel(g, 13, 10, primary);
            drawPixel(g, 14, 9, shadow);
            drawPixel(g, 13, 8, shadow);

            // Under Head Shadow
            drawPixel(g, 7, 10, shadow);
            drawPixel(g, 8, 10, shadow);
            drawPixel(g, 9, 10, shadow);
            drawPixel(g, 10, 11, shadow);
            drawPixel(g, 11, 10, shadow);

            g.dispose();

            File texFile = new File(texturesDir, name + ".png");
            ImageIO.write(img, "png", texFile);

            // 2. Generate Item Model JSON
            File modelFile = new File(modelsDir, name + ".json");
            try (FileWriter writer = new FileWriter(modelFile)) {
                writer.write("{\n");
                writer.write("  \"parent\": \"minecraft:item/handheld\",\n");
                writer.write("  \"textures\": {\n");
                writer.write("    \"layer0\": \"ultimatepickaxes:item/" + name + "\"\n");
                writer.write("  }\n");
                writer.write("}\n");
            }
        }

        System.out.println("Generated 30 16x16 Pickaxe PNG textures and 30 Item Model JSONs successfully!");
    }

    private static void drawPixel(Graphics2D g, int x, int y, Color c) {
        g.setColor(c);
        // Flips Y for standard 16x16 top-down rendering
        g.fillRect(x, 15 - y, 1, 1);
    }
}
