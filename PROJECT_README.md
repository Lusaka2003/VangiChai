# VangtiChai — CSE 489 Assignment 1

An Android app that takes a Taka amount typed on a hand-built numeric
keypad and shows the change breakdown in notes (500, 100, 50, 20, 10, 5,
2, 1), with dedicated layouts for phone/tablet × portrait/landscape.

## How to open this project

1. Open **Android Studio** (Hedgehog/2023.1+ recommended).
2. **File → Open** and select this `VangtiChai/` folder.
3. Android Studio will offer to generate the Gradle wrapper jar/scripts
   automatically on first sync (this repo ships `gradle/wrapper/gradle-wrapper.properties`
   pointing at Gradle 8.4, but not the binary `gradle-wrapper.jar` — Android
   Studio creates that for you, or you can run `gradle wrapper` once if you
   have a system Gradle installed).
4. Let Gradle sync, then **Run ▶** on an emulator.

## Recommended emulators (per assignment spec)

- **Pixel XL** — phone, 411×731 dp — test portrait and landscape.
- **Nexus 10** — tablet, 800×1280 dp — test portrait and landscape.

(System Image: any API ≥ 21 works; API 33/34 recommended for the emulator UI.)

## Project structure

```
app/src/main/
├── AndroidManifest.xml
├── java/com/example/vangtichai/
│   └── MainActivity.kt          # all app logic (entry, change calc, save/restore state)
└── res/
    ├── layout/activity_main.xml             # phone, portrait
    ├── layout-land/activity_main.xml        # phone, landscape
    ├── layout-sw600dp/activity_main.xml     # tablet, portrait
    ├── layout-sw600dp-land/activity_main.xml# tablet, landscape
    ├── values/
    │   ├── sizes.xml      # ALL dimens for phone (no hardcoded values in XML)
    │   ├── styles.xml      # NoteRowStyle / KeyStyle shared styles
    │   ├── strings.xml
    │   ├── colors.xml
    │   └── themes.xml
    └── values-sw600dp/
        └── sizes.xml       # tablet overrides of the same dimens
```

## Key design decisions

- **ConstraintLayout** anchors the title, a vertical `Guideline`, the notes
  table, and the keypad in each of the 4 layout files; only the XML
  changes per configuration — `MainActivity.kt` is 100% shared.
- **GridLayout** (3×4 on phones, 4×3 on tablet-landscape to mirror the
  assignment's tablet-landscape screenshot) lays out the digit/clear keys,
  with each button column-weighted so it stretches evenly.
- **No hardcoded dimensions** anywhere in layout XML — every text size,
  margin, and padding value is a `@dimen` reference resolved from
  `values/sizes.xml` (phone) or `values-sw600dp/sizes.xml` (tablet).
- **State survival on rotation**: the Activity is *not* exempted from
  recreation via `android:configChanges`; instead `onSaveInstanceState` /
  the `savedInstanceState` parameter of `onCreate` persist the typed digit
  string, which is the standard, examiner-expected way to demonstrate this
  requirement.
- **Digit entry from the right**: implemented as plain string
  concatenation (`amountDigits + digit`), matching the spec's example
  (2 → 23 → 234).
- **Change calculation**: a simple greedy algorithm over
  `[500,100,50,20,10,5,2,1]`, which is provably optimal for this note set
  since each denomination divides evenly into "enough" of the next one up
  (standard canonical coin/note system).

## Filling in README.txt

`README.txt` (required by the assignment) lists the devices tested. Update
the "ADDITIONAL DEVICES TESTED" section with whatever extra
emulators/screen sizes you actually ran, before submitting.
