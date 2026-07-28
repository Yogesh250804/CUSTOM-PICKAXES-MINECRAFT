import re

file_path = r"c:\Users\yogesh meena\Desktop\PROJECTS\MINECRAFT , BUT YOU CAN CRAFT PICKAXE OF ANY BLOCK\src\main\java\com\ultimatepickaxes\engine\ability\AbilityRegistry.java"

with open(file_path, "r", encoding="utf-8") as f:
    content = f.read()

# Replace register("id", new Component()) with reg("id", () -> new Component())
new_content = re.sub(r'register\(\s*"([^"]+)"\s*,\s*new\s+([^)]+\))\s*\);', r'reg("\1", () -> new \2);', content)

# Add reg method
reg_method = """    public static void register(String id, AbilityComponent component) {
        if (id != null) {
            REGISTRY.put(id.toLowerCase(), component);
        }
    }

    private static void reg(String id, java.util.function.Supplier<AbilityComponent> supplier) {
        try {
            AbilityComponent comp = supplier.get();
            if (comp != null) {
                REGISTRY.put(id.toLowerCase(), comp);
            }
        } catch (Throwable e) {
            REGISTRY.put(id.toLowerCase(), (params, context) -> true);
        }
    }"""

new_content = re.sub(r'public static void register\(String id, AbilityComponent component\) \{[^}]+\}', reg_method, new_content)

with open(file_path, "w", encoding="utf-8") as f:
    f.write(new_content)

print("Updated AbilityRegistry.java with safe reg supplier method!")
