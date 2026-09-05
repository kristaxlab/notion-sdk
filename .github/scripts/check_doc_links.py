#!/usr/bin/env python3
"""Verify every relative Markdown link in the repository's documentation.

Checks that each link target exists and, when a link carries an ``#anchor``, that the target
document has a heading producing that anchor. External links (http, https, mailto) are skipped so
the check stays offline and deterministic.

Usage: python3 .github/scripts/check_doc_links.py
"""

from __future__ import annotations

import re
import subprocess
import sys
from pathlib import Path

LINK = re.compile(r"\]\(\s*([^)\s]+?)\s*\)")
HEADING = re.compile(r"^#{1,6}\s+(.*?)\s*#*$", re.MULTILINE)
FENCED_BLOCK = re.compile(r"^(?P<fence>```|~~~).*?^(?P=fence)", re.MULTILINE | re.DOTALL)
INLINE_CODE = re.compile(r"`[^`\n]*`")
SKIP_PREFIXES = ("http://", "https://", "mailto:", "#")


def strip_code(content: str) -> str:
    """Drop fenced blocks and inline spans so link syntax shown as an example is not checked."""
    without_blocks = FENCED_BLOCK.sub("", content)
    return INLINE_CODE.sub("", without_blocks)


def repo_root() -> Path:
    out = subprocess.run(
        ["git", "rev-parse", "--show-toplevel"],
        check=True,
        capture_output=True,
        text=True,
    )
    return Path(out.stdout.strip())


def tracked_markdown(root: Path) -> list[Path]:
    out = subprocess.run(
        ["git", "ls-files", "--cached", "--others", "--exclude-standard", "*.md"],
        check=True,
        capture_output=True,
        text=True,
        cwd=root,
    )
    return sorted({root / line for line in out.stdout.splitlines() if line})


def slugify(heading: str) -> str:
    """Reproduce GitHub's heading-to-anchor conversion."""
    text = heading.strip().lower()
    text = re.sub(r"`([^`]*)`", r"\1", text)
    text = re.sub(r"\[([^\]]*)\]\([^)]*\)", r"\1", text)  # links keep their text
    text = re.sub(r"[*_~]", "", text)
    text = re.sub(r"[^\w\s-]", "", text)
    # GitHub replaces each whitespace character with a hyphen rather than collapsing runs, so
    # "Step 4 — tests" becomes "step-4--tests" once the em dash is stripped.
    return re.sub(r"\s", "-", text.strip())


def anchors(path: Path) -> set[str]:
    try:
        content = path.read_text(encoding="utf-8")
    except (OSError, UnicodeDecodeError):
        return set()
    return {slugify(h) for h in HEADING.findall(content)}


def main() -> int:
    root = repo_root()
    anchor_cache: dict[Path, set[str]] = {}
    problems: list[str] = []
    checked = 0

    for doc in tracked_markdown(root):
        if not doc.exists():
            continue
        content = strip_code(doc.read_text(encoding="utf-8"))
        rel_doc = doc.relative_to(root)

        for target in LINK.findall(content):
            if target.startswith(SKIP_PREFIXES):
                continue

            path_part, _, anchor = target.partition("#")
            if not path_part:
                continue

            checked += 1
            resolved = (doc.parent / path_part).resolve()

            if not resolved.exists():
                problems.append(f"missing file    {rel_doc} -> {target}")
                continue

            if anchor and resolved.suffix == ".md":
                if resolved not in anchor_cache:
                    anchor_cache[resolved] = anchors(resolved)
                if anchor.lower() not in anchor_cache[resolved]:
                    problems.append(f"missing anchor  {rel_doc} -> {target}")

    if problems:
        print("\n".join(sorted(problems)))
        print(f"\n{len(problems)} broken documentation link(s) out of {checked} checked.")
        return 1

    print(f"All {checked} relative documentation links resolve.")
    return 0


if __name__ == "__main__":
    sys.exit(main())
