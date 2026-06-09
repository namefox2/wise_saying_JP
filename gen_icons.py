#!/usr/bin/env python3
"""
Usage: python3 gen_icons.py <source_image.png>
Generates all Android mipmap launcher icon sizes from a square source image.
"""
import sys
import os
from PIL import Image

sizes = {
    "mipmap-mdpi":    48,
    "mipmap-hdpi":    72,
    "mipmap-xhdpi":   96,
    "mipmap-xxhdpi":  144,
    "mipmap-xxxhdpi": 192,
}

RES_DIR = os.path.join(
    os.path.dirname(__file__),
    "../wise_saying_JP/app/src/main/res"
)

def gen(src_path):
    img = Image.open(src_path).convert("RGBA")
    w, h = img.size
    if w != h:
        side = min(w, h)
        img = img.crop(((w - side) // 2, (h - side) // 2,
                        (w + side) // 2, (h + side) // 2))
        print(f"Cropped to square {side}×{side}")

    for folder, size in sizes.items():
        out_dir = os.path.join(RES_DIR, folder)
        os.makedirs(out_dir, exist_ok=True)
        resized = img.resize((size, size), Image.LANCZOS)
        for name in ("ic_launcher.png", "ic_launcher_round.png"):
            out_path = os.path.join(out_dir, name)
            resized.save(out_path, "PNG")
            print(f"  ✓ {folder}/{name}  ({size}×{size})")

    # Also write a 512×512 for Play Store
    play_store = os.path.join(RES_DIR, "..", "..", "play_store_icon.png")
    img.resize((512, 512), Image.LANCZOS).save(play_store, "PNG")
    print(f"  ✓ play_store_icon.png (512×512)")

if __name__ == "__main__":
    if len(sys.argv) < 2:
        print("Usage: python3 gen_icons.py <source_image.png>")
        sys.exit(1)
    gen(sys.argv[1])
    print("\nDone! Rebuild the app to apply new icons.")
