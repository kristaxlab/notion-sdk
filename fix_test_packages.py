import os, shutil

TEST_ROOT = "/Users/krista/Documents/Coder/notion-sdk/src/test/java/io/kristixlab/notion/api/http"

moves = [
    # (old_path, new_path, old_pkg, new_pkg, old_class, new_class)
    (
        f"{TEST_ROOT}/request/RequestTargetTest.java",
        f"{TEST_ROOT}/request/ApiPathTest.java",
        None, None,
        "class URLInfoTest {", "class ApiPathTest {"
    ),
    (
        f"{TEST_ROOT}/transport/InterceptingHttpClientTest.java",
        f"{TEST_ROOT}/client/InterceptingHttpClientTest.java",
        "package io.kristixlab.notion.api.http.transport;",
        "package io.kristixlab.notion.api.http.client;",
        None, None
    ),
    (
        f"{TEST_ROOT}/transport/refactored/ErrorHandlingHttpClientTest.java",
        f"{TEST_ROOT}/client/ErrorHandlingHttpClientTest.java",
        "package io.kristixlab.notion.api.http.transport.refactored;",
        "package io.kristixlab.notion.api.http.client;",
        None, None
    ),
    (
        f"{TEST_ROOT}/transport/util/HttpBodyFactoryTest.java",
        f"{TEST_ROOT}/request/HttpBodyFactoryTest.java",
        "package io.kristixlab.notion.api.http.transport.util;",
        "package io.kristixlab.notion.api.http.request;",
        None, None
    ),
]

for (old, new, old_pkg, new_pkg, old_cls, new_cls) in moves:
    os.makedirs(os.path.dirname(new), exist_ok=True)
    with open(old) as f:
        src = f.read()
    if old_pkg:
        src = src.replace(old_pkg, new_pkg, 1)
    if old_cls:
        src = src.replace(old_cls, new_cls, 1)
    with open(new, "w") as f:
        f.write(src)
    os.remove(old)
    print(f"Moved  {os.path.basename(old)} -> {new}")

# Remove now-empty stale directories
for stale in [
    f"{TEST_ROOT}/transport/refactored",
    f"{TEST_ROOT}/transport/util",
    f"{TEST_ROOT}/transport",
]:
    if os.path.isdir(stale):
        remaining = os.listdir(stale)
        if not remaining:
            shutil.rmtree(stale)
            print(f"Removed  {stale}")
        else:
            print(f"SKIP (not empty): {stale} -> {remaining}")

print("Done.")

