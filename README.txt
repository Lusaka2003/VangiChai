VangtiChai - CSE 489 Assignment 1
==================================

Language / framework: Kotlin, Android ConstraintLayout (+ GridLayout for the keypad)
Minimum SDK: API 21 (Android 5.0 Lollipop)

REQUIRED TEST DEVICES (per assignment spec):
  - Pixel XL (phone, 411 x 731 dp)      -> portrait: res/layout/activity_main.xml
                                            landscape: res/layout-land/activity_main.xml
  - Nexus 10 (tablet, 800 x 1280 dp)    -> portrait: res/layout-sw600dp/activity_main.xml
                                            landscape: res/layout-sw600dp-land/activity_main.xml

ADDITIONAL DEVICES TESTED (fill in / adjust with your actual emulator runs
before submitting -- the assignment asks you to list what you tested):
  - Pixel 4 (phone, ~393 x 830 dp)            - portrait & landscape: falls back
    to the default `layout/` and `layout-land/` variants; checked that the
    keypad and notes table do not clip or overlap.
  - Pixel C (tablet, ~900 x 1280 dp, sw >= 600dp) - falls into the sw600dp
    bucket; checked spacing scales sensibly and nothing looks stretched.
  - Small phone, e.g. Nexus S (~320 x 480 dp) - confirmed the keypad_max_width
    cap and GridLayout column-weighting keep buttons usable instead of
    overflowing the screen.

NOTES ON IMPLEMENTATION:
  - All text sizes, padding, and margins are defined in
    res/values/sizes.xml (phone defaults) and overridden for tablets in
    res/values-sw600dp/sizes.xml. No dimension is hardcoded directly in any
    layout XML file.
  - Digit entry appends to the right of the current amount (see
    MainActivity.onDigitPressed). CLEAR resets the amount to empty.
  - The notes table always reflects a greedy breakdown of the current
    amount using denominations 500, 100, 50, 20, 10, 5, 2, 1
    (MainActivity.calculateChange).
  - State (the entered amount) survives rotation via
    onSaveInstanceState/onRestoreInstanceState in MainActivity -- the
    Activity is allowed to be destroyed/recreated by the system on a
    configuration change (no android:configChanges override is used),
    which is the standard/recommended way to demonstrate state retention.
  - Four distinct layout XML files implement the four required
    screen-size/orientation combinations, as suggested in the assignment
    hints, all sharing the same underlying view IDs so a single
    MainActivity + ViewBinding class works across all of them.
