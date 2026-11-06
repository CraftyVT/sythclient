# Cosmetix – Fabric Client-Mod (Minecraft 1.21.11)

Capes, Flügel, Hüte und Partikel-Auren. Menü per **Rechte Shift-Taste**, Auswahl wird in `config/cosmetix.json` gespeichert.

## Starten
1. Ordner in IntelliJ IDEA (Java 21) öffnen, Gradle importieren lassen.
   Falls kein Gradle-Wrapper vorhanden ist: `gradle wrapper --gradle-version 9.2.0` (oder Wrapper aus der Fabric-Vorlage kopieren).
2. `./gradlew runClient` zum Testen, `./gradlew build` für die Jar (`build/libs/`).
3. Versionen stehen in `gradle.properties`. Bei "not found" aktuelle Werte von https://fabricmc.net/develop holen.

## Aufbau
| Datei | Zweck |
|---|---|
| `cosmetic/CosmeticRegistry` | Liste aller Cosmetics (hier neue eintragen) |
| `cosmetic/CosmeticManager` | Loadout + Speichern; `get(UUID, type)` ist der Hook fürs spätere Backend |
| `render/CosmeticModels` | Geometrie aller Cosmetics (statische ModelParts) |
| `render/CosmeticFeatureRenderer` | Rendert Cape/Flügel/Hut am Spieler, Animationen via MatrixStack |
| `render/AuraEffects` | Partikel-Auren |
| `mixin/*` | Reicht die Spieler-UUID in den Render-State durch |
| `tools/generate_textures.py` | Erzeugt alle Texturen (Pillow) |

## Neues Cosmetic hinzufügen
1. Eintrag in `CosmeticRegistry`, Textur nach `assets/cosmetix/textures/cosmetic/<typ>/<id>.png`, Name in beide `lang/*.json`.
2. Für Hüte: Geometrie in `CosmeticModels` + `case` in `renderHat`. Für Auren: Methode in `AuraEffects`.

## Wo es bei 1.21.11 haken kann (ungetestet kompiliert!)
1.21.9+ hat das Rendering stark umgebaut. Diese Stellen zuerst prüfen, falls der Compiler meckert:
- `CosmeticRenderLayers`: Name der Layer-Fabrik (`RenderLayers.getEntityCutoutNoCull`).
- `OrderedRenderCommandQueue.submitModelPart(...)` in `CosmeticFeatureRenderer.submit` (Parameterliste).
- `ModelPart.applyTransform(MatrixStack)` für Krone/Hut/Heiligenschein.
- `ClientWorld.addParticleClient(...)` in `AuraEffects`.
- `KeyBinding.Category.create(...)` in `CosmetixClient`.
- Flügel-Drehrichtung: Vorzeichen von `side * flap` in `renderWings`, falls sie nach vorn statt nach hinten schwingen.

## Nächste Schritte
- **Andere Spieler sehen Cosmetics erst mit Backend**: kleiner Server (UUID -> Loadout), `CosmeticManager.get` fragt ihn ab und cached.
- Vorschau des Spielers im Menü, Shop/Freischaltungen, Blockbench-Modelle statt Code-Geometrie.
