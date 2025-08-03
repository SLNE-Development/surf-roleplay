package dev.slne.surf.roleplay.api.mechanic.cash.utils

sealed class CashInInventoryResult {
    /**
     * Represents the result of a cash inventory check, detailing the total amount of cash and the breakdown
     * of different denominations available in the player's inventory.
     *
     * @property totalAmount The total amount of cash in the inventory.
     * @property amount1 The number of 1 currency units.
     * @property amount2 The number of 2 currency units.
     * @property amount5 The number of 5 currency units.
     * @property amount10 The number of 10 currency units.
     * @property amount20 The number of 20 currency units.
     * @property amount50 The number of 50 currency units.
     * @property amount100 The number of 100 currency units.
     * @property amount200 The number of 200 currency units.
     * @property amount500 The number of 500 currency units.
     */
    data class Success(
        val amount1: Int,
        val amount2: Int,
        val amount5: Int,
        val amount10: Int,
        val amount20: Int,
        val amount50: Int,
        val amount100: Int,
        val amount200: Int,
        val amount500: Int
    ) : CashInInventoryResult() {
        val totalAmount: Int
            get() = (amount1 * 1) + (amount2 * 2) + (amount5 * 5) + (amount10 * 10) +
                    (amount20 * 20) + (amount50 * 50) + (amount100 * 100) +
                    (amount200 * 200) + (amount500 * 500)
    }

    /**
     * Represents a failure to retrieve the cash inventory due to the player not being online.
     */
    object PlayerNotOnline : CashInInventoryResult()
}
