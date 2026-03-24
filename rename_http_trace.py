import os, re

ROOT = "/Users/krista/Documents/Coder/notion-sdk/src"

RENAMES = {
    "RqRsCatchInterceptor": "ExchangeRecordingInterceptor",
    "exchangeLogging":      "recordExchanges",
}

FILE_MOVES = [
    (
        "main/java/io/kristixlab/notion/api/http/interceptor/RqRsCatchInterceptor.java",
        "main/java/io/kristixlab/notion/api/http/interceptor/ExchangeRecordingInterceptor.java",
    ),
]

def apply_renames(content):
    for old, new in RENAMES.items():
        content = re.sub(r'\b' + old + r'\b', new, content)
    return content

# 1. Rewrite all .java files
for dirpath, _, filenames in os.walk(ROOT):
    for fname in filenames:
        if not fname.endswith(".java"):
            continue
        path = os.path.join(dirpath, fname)
        with open(path, encoding="utf-8") as f:
            original = f.read()
        updated = apply_renames(original)
        if updated != original:
            with open(path, "w", encoding="utf-8") as f:
                f.write(updated)
            print(f"Updated  {path}")

# 2. Rename files
for rel_old, rel_new in FILE_MOVES:
    old_path = os.path.join(ROOT, rel_old)
    new_path = os.path.join(ROOT, rel_new)
    if os.path.exists(old_path):
        os.rename(old_path, new_path)
        print(f"Moved    {os.path.basename(old_path)} -> {os.path.basename(new_path)}")
    else:
        print(f"SKIP (not found): {old_path}")

print("Done.")
