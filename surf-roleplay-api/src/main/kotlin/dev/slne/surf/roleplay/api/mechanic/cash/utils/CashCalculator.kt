package dev.slne.surf.roleplay.api.mechanic.cash.utils

object CashCalculator {
    /**
     * Calculates the breakdown of cash denominations for a given amount.
     *
     * This function takes an integer amount and returns a [CashCalculatorResult] containing the number of each denomination needed to make up that amount.
     * The denominations considered are 500, 200, 100, 50, 20, 10, 5, 2, and 1.
     * If the amount is negative or zero, it will return a [CashCalculatorResult.NotCalculable] indicating that the calculation cannot be performed.
     *
     * @param amount The total amount of cash to be calculated. Must be a positive integer.
     * @return A [CashCalculatorResult] containing the breakdown of cash denominations.
     */
    fun calculateCash(amount: Int): CashCalculatorResult {
        if (amount <= 0) {
            return CashCalculatorResult.NotCalculable(amount, 0)
        }

        var remainingAmount = amount

        val amount500 = remainingAmount / 500
        remainingAmount %= 500

        val amount200 = remainingAmount / 200
        remainingAmount %= 200

        val amount100 = remainingAmount / 100
        remainingAmount %= 100

        val amount50 = remainingAmount / 50
        remainingAmount %= 50

        val amount20 = remainingAmount / 20
        remainingAmount %= 20

        val amount10 = remainingAmount / 10
        remainingAmount %= 10

        val amount5 = remainingAmount / 5
        remainingAmount %= 5

        val amount2 = remainingAmount / 2
        val amount1 = remainingAmount % 2

        return CashCalculatorResult.Success(
            amount1 = amount1,
            amount2 = amount2,
            amount5 = amount5,
            amount10 = amount10,
            amount20 = amount20,
            amount50 = amount50,
            amount100 = amount100,
            amount200 = amount200,
            amount500 = amount500
        )
    }

    sealed class CashCalculatorResult {
        /**
         * Represents a successful calculation of cash denominations.
         *
         * @param amount1 The number of 1 currency coins.
         * @param amount2 The number of 2 currency coins.
         * @param amount5 The number of 5 currency notes.
         * @param amount10 The number of 10 currency notes.
         * @param amount20 The number of 20 currency notes.
         * @param amount50 The number of 50 currency notes.
         * @param amount100 The number of 100 currency notes.
         * @param amount200 The number of 200 currency notes.
         * @param amount500 The number of 500 currency notes.
         */
        class Success(
            val amount1: Int,
            val amount2: Int,
            val amount5: Int,
            val amount10: Int,
            val amount20: Int,
            val amount50: Int,
            val amount100: Int,
            val amount200: Int,
            val amount500: Int
        ) : CashCalculatorResult()

        /**
         * Represents a case where the cash amount cannot be calculated.
         *
         * @param amount The total amount of cash that was attempted to be calculated.
         * @param remainingAmount The remaining amount that could not be calculated.
         */
        class NotCalculable(val amount: Int, val remainingAmount: Int) : CashCalculatorResult()
    }
}