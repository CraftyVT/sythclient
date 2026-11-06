"""Generiert alle Cosmetic-Texturen. Ausführen im Projektordner: python3 tools/generate_textures.py (braucht Pillow)."""
import math, os, random
from PIL import Image

random.seed(7)
OUT = "src/main/resources/assets/cosmetix/textures/cosmetic"


def lerp(a, b, t):
    return tuple(int(a[i] + (b[i] - a[i]) * t) for i in range(3))


def grad(stops, t):
    for (p0, c0), (p1, c1) in zip(stops, stops[1:]):
        if t <= p1:
            return lerp(c0, c1, (t - p0) / (p1 - p0) if p1 > p0 else 0)
    return stops[-1][1]


def shade(c, f):
    return tuple(max(0, min(255, int(v * f))) for v in c)


def save(img, kind, name):
    d = os.path.join(OUT, kind)
    os.makedirs(d, exist_ok=True)
    img.save(os.path.join(d, name + ".png"))


def cape(name, stops, trim, sparkle=0.04, flames=False):
    img = Image.new("RGBA", (64, 32), shade(trim, 0.5) + (255,))
    px = img.load()
    flame_h = [random.randint(2, 7) for _ in range(10)]
    for y in range(16):
        for x in range(10):
            c = grad(stops, y / 15)
            c = shade(c, 1 + math.sin(x * 0.9 + y * 0.55) * 0.07)
            if flames and y >= 16 - flame_h[x]:
                k = (y - (16 - flame_h[x])) / flame_h[x]
                c = lerp((255, 120, 20), (255, 225, 90), k)
            if x in (0, 9) or y in (0, 15):
                c = trim
            elif random.random() < sparkle:
                c = lerp(c, (255, 255, 255), 0.8)
            px[1 + x, 1 + y] = c + (255,)      # Außenseite
            px[12 + x, 1 + y] = shade(c, 0.55) + (255,)  # Innenseite
    return img


def wings(c0, c1, line=None):
    img = Image.new("RGBA", (64, 32), (0, 0, 0, 0))
    px = img.load()
    for y in range(32):
        for x in range(64):
            c = lerp(c0, c1, x / 63)
            c = shade(c, 0.92 + 0.08 * math.sin(x * 1.7 + (y // 5) * 2.1) + random.uniform(-0.03, 0.03))
            if y % 5 == 4:
                c = shade(c, 0.7)
            if line and y % 5 == 0:
                c = line
            px[x, y] = c + (255,)
    return img


def hat(stops, accent=None, stars=False, noise=0.05):
    img = Image.new("RGBA", (64, 64), (0, 0, 0, 255))
    px = img.load()
    for y in range(64):
        for x in range(64):
            c = grad(stops, y / 63)
            c = shade(c, 1 + random.uniform(-noise, noise) + (0.12 if (x // 4 + y // 4) % 5 == 0 else 0))
            px[x, y] = c + (255,)
    if stars:
        for _ in range(26):
            sx, sy = random.randint(1, 62), random.randint(1, 62)
            for dx, dy in ((0, 0), (1, 0), (-1, 0), (0, 1), (0, -1)):
                px[sx + dx, sy + dy] = accent + (255,)
    return img


# Capes
save(cape("aurora", [(0, (20, 230, 200)), (0.5, (110, 90, 255)), (1, (190, 60, 200))], (235, 250, 255), 0.06), "cape", "aurora")
save(cape("ember", [(0, (35, 8, 8)), (0.6, (140, 30, 10)), (1, (255, 120, 20))], (255, 170, 60), 0.02, True), "cape", "ember")

# Flügel
save(wings((255, 255, 255), (255, 232, 160)), "wings", "angel")
save(wings((25, 8, 55), (135, 45, 210), line=(90, 255, 255)), "wings", "phantom")

# Hüte
save(hat([(0, (255, 232, 90)), (1, (200, 145, 20))]), "hat", "crown")
save(hat([(0, (255, 252, 215)), (1, (255, 236, 150))], noise=0.02), "hat", "halo")
save(hat([(0, (95, 45, 180)), (1, (55, 20, 120))], accent=(255, 225, 90), stars=True), "hat", "wizard")
print("Texturen erzeugt.")
