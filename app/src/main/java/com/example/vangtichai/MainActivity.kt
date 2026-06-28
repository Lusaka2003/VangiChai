package com.example.vangtichai

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.vangtichai.databinding.ActivityMainBinding

/**
 * VangtiChai (Taka change calculator)
 *
 * Behaviour:
 *  - Tapping a digit key appends that digit to the right of the currently
 *    entered amount (e.g. tapping 2, 3, 4 in order shows 2 -> 23 -> 234).
 *  - The CLEAR key resets the amount back to empty/zero.
 *  - The notes table on screen always reflects the greedy breakdown of the
 *    current amount into Taka notes: 500, 100, 50, 20, 10, 5, 2, 1.
 *  - The same Activity + same logic is reused across all four layout
 *    variants (phone portrait, phone landscape, tablet portrait, tablet
 *    landscape); only the XML differs per screen configuration.
 *  - State (the entered amount) is preserved across configuration changes
 *    (e.g. rotation) via onSaveInstanceState/onRestoreInstanceState, since
 *    we deliberately let the system recreate the Activity on rotation
 *    rather than locking orientation handling via configChanges.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // The entered amount, stored as a string of digits so we can easily
    // append from the right and distinguish "no amount entered yet" (empty)
    // from "0" typed by the user.
    private var amountDigits: String = ""

    // Denominations available for Taka notes, in descending order, as
    // required by the assignment.
    private val noteDenominations = intArrayOf(500, 100, 50, 20, 10, 5, 2, 1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Restore previously entered amount, if any (e.g. after rotation).
        savedInstanceState?.let {
            amountDigits = it.getString(KEY_AMOUNT, "") ?: ""
        }

        setupKeypadListeners()
        refreshUi()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_AMOUNT, amountDigits)
    }

    /** Wires up the 10 digit buttons and the clear button. */
    private fun setupKeypadListeners() {
        val digitButtons = listOf(
            binding.btn0 to "0",
            binding.btn1 to "1",
            binding.btn2 to "2",
            binding.btn3 to "3",
            binding.btn4 to "4",
            binding.btn5 to "5",
            binding.btn6 to "6",
            binding.btn7 to "7",
            binding.btn8 to "8",
            binding.btn9 to "9"
        )

        for ((button, digit) in digitButtons) {
            button.setOnClickListener { onDigitPressed(digit) }
        }

        binding.btnClear.setOnClickListener { onClearPressed() }
    }

    /** Appends [digit] to the right of the current amount, then refreshes the UI. */
    private fun onDigitPressed(digit: String) {
        // Avoid an unbounded string of leading zeros (e.g. "0" + "0" + "0"...).
        amountDigits = if (amountDigits == "0") digit else amountDigits + digit

        // Guard against overflow from absurdly long input (defensive only;
        // a Long comfortably covers any realistic Taka amount).
        if (amountDigits.length > 15) {
            amountDigits = amountDigits.takeLast(15)
        }

        refreshUi()
    }

    /** Resets the entered amount back to empty. */
    private fun onClearPressed() {
        amountDigits = ""
        refreshUi()
    }

    /** Re-renders the "Taka:" label and the notes table from [amountDigits]. */
    private fun refreshUi() {
        val amountText = amountDigits // empty string -> just "Taka:" with nothing after
        binding.tvTaka.text = if (amountText.isEmpty()) {
            getString(R.string.taka_label)
        } else {
            getString(R.string.taka_label_format, amountText)
        }

        val amountValue = amountDigits.toLongOrNull() ?: 0L
        val noteCounts = calculateChange(amountValue)

        binding.row500.text = getString(R.string.note_row_format, 500, noteCounts[500] ?: 0)
        binding.row100.text = getString(R.string.note_row_format, 100, noteCounts[100] ?: 0)
        binding.row50.text = getString(R.string.note_row_format, 50, noteCounts[50] ?: 0)
        binding.row20.text = getString(R.string.note_row_format, 20, noteCounts[20] ?: 0)
        binding.row10.text = getString(R.string.note_row_format, 10, noteCounts[10] ?: 0)
        binding.row5.text = getString(R.string.note_row_format, 5, noteCounts[5] ?: 0)
        binding.row2.text = getString(R.string.note_row_format, 2, noteCounts[2] ?: 0)
        binding.row1.text = getString(R.string.note_row_format, 1, noteCounts[1] ?: 0)
    }

    /**
     * Greedily breaks [amount] down into the fewest possible notes from
     * [noteDenominations] (500, 100, 50, 20, 10, 5, 2, 1). Since 1 is among
     * the denominations, any non-negative integer amount can always be
     * represented exactly.
     */
    private fun calculateChange(amount: Long): Map<Int, Int> {
        var remaining = amount
        val counts = LinkedHashMap<Int, Int>()
        for (note in noteDenominations) {
            val count = (remaining / note).toInt()
            counts[note] = count
            remaining -= count.toLong() * note
        }
        return counts
    }

    // KEY_AMOUNT mirrors R.string.key_state_amount; kept as a Kotlin constant
    // (rather than calling getString() before the Activity is fully set up)
    // but the literal must always match strings.xml/key_state_amount.
    companion object {
        private const val KEY_AMOUNT = "KEY_AMOUNT_STRING"
    }
}
