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
        // Original 30
        pickaxeColors.put("dirt_pickaxe", new Color[]{new Color(134, 96, 67), new Color(87, 61, 38), new Color(175, 128, 91)});
        pickaxeColors.put("cobblestone_pickaxe", new Color[]{new Color(120, 120, 120), new Color(80, 80, 80), new Color(160, 160, 160)});
        pickaxeColors.put("tnt_pickaxe", new Color[]{new Color(219, 57, 43), new Color(168, 36, 24), new Color(245, 245, 245)});
        pickaxeColors.put("glass_pickaxe", new Color[]{new Color(188, 231, 236, 220), new Color(128, 194, 204, 180), new Color(232, 250, 252, 255)});
        pickaxeColors.put("sponge_pickaxe", new Color[]{new Color(194, 184, 54), new Color(140, 133, 35), new Color(225, 215, 80)});
        pickaxeColors.put("honey_pickaxe", new Color[]{new Color(237, 157, 27, 240), new Color(180, 110, 15, 220), new Color(247, 194, 94, 255)});
        pickaxeColors.put("magma_pickaxe", new Color[]{new Color(232, 85, 23), new Color(59, 19, 14), new Color(247, 148, 29)});
        pickaxeColors.put("slime_pickaxe", new Color[]{new Color(89, 179, 74, 230), new Color(50, 120, 40, 210), new Color(133, 224, 119, 255)});
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

        // 20 NEW Pickaxes
        pickaxeColors.put("beacon_pickaxe", new Color[]{new Color(110, 225, 240), new Color(40, 140, 160), new Color(220, 255, 255)});
        pickaxeColors.put("bookshelf_pickaxe", new Color[]{new Color(160, 105, 55), new Color(100, 60, 30), new Color(210, 160, 90)});
        pickaxeColors.put("copper_pickaxe", new Color[]{new Color(195, 108, 77), new Color(135, 68, 45), new Color(235, 158, 127)});
        pickaxeColors.put("ender_chest_pickaxe", new Color[]{new Color(32, 54, 56), new Color(15, 28, 30), new Color(60, 180, 140)});
        pickaxeColors.put("bedrock_pickaxe", new Color[]{new Color(50, 50, 50), new Color(25, 25, 25), new Color(85, 85, 85)});
        pickaxeColors.put("crying_obsidian_pickaxe", new Color[]{new Color(78, 26, 120), new Color(38, 8, 65), new Color(165, 75, 235)});
        pickaxeColors.put("hay_bale_pickaxe", new Color[]{new Color(205, 175, 40), new Color(145, 115, 20), new Color(245, 215, 80)});
        pickaxeColors.put("ice_pickaxe", new Color[]{new Color(145, 200, 255), new Color(85, 140, 205), new Color(215, 240, 255)});
        pickaxeColors.put("moss_pickaxe", new Color[]{new Color(90, 130, 45), new Color(55, 85, 25), new Color(140, 185, 75)});
        pickaxeColors.put("nylium_pickaxe", new Color[]{new Color(185, 30, 40), new Color(115, 15, 20), new Color(245, 80, 90)});
        pickaxeColors.put("prismarine_bricks_pickaxe", new Color[]{new Color(100, 170, 155), new Color(60, 115, 100), new Color(155, 215, 200)});
        pickaxeColors.put("purpur_pickaxe", new Color[]{new Color(170, 125, 170), new Color(110, 75, 110), new Color(215, 175, 215)});
        pickaxeColors.put("quartz_pickaxe", new Color[]{new Color(235, 230, 225), new Color(185, 175, 165), new Color(255, 255, 255)});
        pickaxeColors.put("soul_sand_pickaxe", new Color[]{new Color(85, 62, 50), new Color(45, 30, 20), new Color(125, 95, 80)});
        pickaxeColors.put("target_pickaxe", new Color[]{new Color(225, 215, 195), new Color(185, 30, 30), new Color(255, 255, 255)});
        pickaxeColors.put("terracotta_pickaxe", new Color[]{new Color(150, 90, 60), new Color(100, 55, 35), new Color(200, 130, 95)});
        pickaxeColors.put("warped_pickaxe", new Color[]{new Color(45, 145, 145), new Color(20, 90, 90), new Color(90, 200, 190)});
        pickaxeColors.put("wool_pickaxe", new Color[]{new Color(235, 235, 235), new Color(185, 185, 185), new Color(255, 255, 255)});
        pickaxeColors.put("bone_pickaxe", new Color[]{new Color(225, 220, 200), new Color(175, 170, 150), new Color(255, 250, 235)});
        pickaxeColors.put("enchanting_table_pickaxe", new Color[]{new Color(125, 30, 30), new Color(45, 55, 65), new Color(85, 185, 215)});

        for (Map.Entry<String, Color[]> entry : pickaxeColors.entrySet()) {
            String name = entry.getKey();
            Color mainColor = entry.getValue()[0];
            Color darkColor = entry.getValue()[1];
            Color lightColor = entry.getValue()[2];

            BufferedImage img = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = img.createGraphics();

            Color stickColor = new Color(134, 96, 67);
            Color stickDark = new Color(87, 61, 38);

            // Handle Pixels
            setPx(img, 2, 13, stickDark);
            setPx(img, 3, 12, stickColor);
            setPx(img, 4, 11, stickColor);
            setPx(img, 5, 10, stickColor);
            setPx(img, 6, 9, stickColor);
            setPx(img, 7, 8, stickColor);
            setPx(img, 8, 7, stickColor);
            setPx(img, 9, 6, stickDark);

            // Pickaxe Head
            setPx(img, 6, 2, lightColor);
            setPx(img, 7, 2, lightColor);
            setPx(img, 8, 2, lightColor);
            setPx(img, 9, 2, lightColor);
            setPx(img, 10, 2, lightColor);
            setPx(img, 11, 3, lightColor);
            setPx(img, 12, 3, lightColor);
            setPx(img, 13, 4, lightColor);

            setPx(img, 5, 3, lightColor);
            setPx(img, 4, 4, lightColor);
            setPx(img, 3, 5, lightColor);

            setPx(img, 6, 3, mainColor);
            setPx(img, 7, 3, mainColor);
            setPx(img, 8, 3, mainColor);
            setPx(img, 9, 3, mainColor);
            setPx(img, 10, 3, mainColor);
            setPx(img, 11, 4, mainColor);
            setPx(img, 12, 4, mainColor);

            setPx(img, 5, 4, mainColor);
            setPx(img, 4, 5, mainColor);

            setPx(img, 2, 6, darkColor);
            setPx(img, 3, 6, mainColor);
            setPx(img, 3, 7, darkColor);
            setPx(img, 4, 6, darkColor);

            setPx(img, 13, 5, darkColor);
            setPx(img, 13, 6, mainColor);
            setPx(img, 14, 6, darkColor);
            setPx(img, 13, 7, darkColor);

            setPx(img, 7, 4, darkColor);
            setPx(img, 8, 4, darkColor);
            setPx(img, 9, 4, darkColor);
            setPx(img, 10, 4, darkColor);
            setPx(img, 8, 5, darkColor);
            setPx(img, 9, 5, darkColor);

            g.dispose();

            File texFile = new File(texturesDir, name + ".png");
            ImageIO.write(img, "png", texFile);

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

        System.out.println("Generated 50 pixel-perfect pickaxe textures and handheld models successfully!");
    }

    private static void setPx(BufferedImage img, int x, int y, Color c) {
        if (x >= 0 && x < 16 && y >= 0 && y < 16) {
            img.setRGB(x, y, c.getRGB());
        }
    }
}
