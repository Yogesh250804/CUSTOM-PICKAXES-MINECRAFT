import subprocess
import os

BASE_DIR = r"c:\Users\yogesh meena\Desktop\PROJECTS\MINECRAFT , BUT YOU CAN CRAFT PICKAXE OF ANY BLOCK"
BUILD_MAIN = os.path.join(BASE_DIR, r"build\classes\java\main")
BUILD_TEST = os.path.join(BASE_DIR, r"build\classes\java\test")
RES_MAIN = os.path.join(BASE_DIR, r"src\main\resources")

cp_entries = [BUILD_MAIN, BUILD_TEST, RES_MAIN]

gradle_cache = os.path.expanduser(r"~\.gradle\caches")
loom_jars = []
other_jars = []

for root, dirs, files in os.walk(gradle_cache):
    for f in files:
        if f.endswith(".jar"):
            full_p = os.path.join(root, f)
            if "fabric-loom" in full_p or "minecraft" in f.lower():
                loom_jars.append(full_p)
            else:
                other_jars.append(full_p)

# Put Loom jars first
cp_entries.extend(loom_jars)
cp_entries.extend(other_jars)

cp_joined = ";".join(cp_entries).replace("\\", "/")

arg_file = os.path.join(BASE_DIR, "scratch", "java_args.txt").replace("\\", "/")
with open(arg_file, "w", encoding="utf-8") as f:
    f.write("-cp\n")
    f.write(f'"{cp_joined}"\n')
    f.write("VerifyAllPickaxesTest\n")

res = subprocess.run(["java", f"@{arg_file}"], capture_output=True, text=True, cwd=BASE_DIR)
print("STDOUT:")
print(res.stdout)
print("STDERR:")
print(res.stderr)
