package dev.slne.surf.roleplay.api.transaction

import dev.slne.surf.roleplay.api.player.utils.BalanceType

interface HasTransactions {

    /**
     * Returns the player's balance for the specified [BalanceType].
     *
     * @param balanceType The type of balance to retrieve.
     * @return The balance amount as a [Double].
     */
    suspend fun getBalance(balanceType: BalanceType): Double

    /**
     * Adds the specified [amount] to the player's balance of the given [BalanceType].
     *
     * @param balanceType The type of balance to add to.
     * @param amount The amount to add to the balance.
     */
    suspend fun addBalance(balanceType: BalanceType, amount: Double)

    /**
     * Removes the specified [amount] from the player's balance of the given [BalanceType].
     *
     * @param balanceType The type of balance to remove from.
     * @param amount The amount to remove from the balance.
     */
    suspend fun removeBalance(balanceType: BalanceType, amount: Double)

    /**
     * Retrieves the balance history for the specified [BalanceType].
     *
     * @param balanceType The type of balance to retrieve history for.
     * @param limit The maximum number of transactions to retrieve. Defaults to 10.
     * @return A list of [RpTransaction] representing the balance history.
     */
    suspend fun getBalanceHistory(
        balanceType: BalanceType,
        limit: Int = 10
    ): List<RpTransaction>

    /**
     * Checks if the player has a sufficient balance of the specified [BalanceType] for the given [amount].
     *
     * @param balanceType The type of balance to check.
     * @param amount The amount to check against the player's balance.
     * @return `true` if the player has enough balance, `false` otherwise.
     */
    suspend fun hasBalance(
        balanceType: BalanceType,
        amount: Double
    ): Boolean

    /**
     * Retrieves the player's cash balance.
     *
     * @return The cash balance as a [Double].
     */
    suspend fun getCashBalance(): Double

    /**
     * Adds the specified [amount] to the player's cash balance.
     *
     * @param amount The amount to add to the cash balance.
     */
    suspend fun addCashBalance(amount: Double)

    /**
     * Removes the specified [amount] from the player's cash balance.
     *
     * @param amount The amount to remove from the cash balance.
     */
    suspend fun removeCashBalance(amount: Double)

    /**
     * Checks if the player has a sufficient cash balance for the given [amount].
     *
     * @param amount The amount to check against the player's cash balance.
     * @return `true` if the player has enough cash balance, `false` otherwise.
     */
    suspend fun hasCashBalance(amount: Double): Boolean

    /**
     * Retrieves the cash balance history.
     *
     * @param limit The maximum number of transactions to retrieve. Defaults to 10.
     * @return A list of [RpTransaction] representing the cash balance history.
     */
    suspend fun getCashBalanceHistory(limit: Int = 10): List<RpTransaction>

    /**
     * Retrieves the player's bank balance.
     *
     * @return The bank balance as a [Double].
     */
    suspend fun getBankBalance(): Double

    /**
     * Adds the specified [amount] to the player's bank balance.
     *
     * @param amount The amount to add to the bank balance.
     */
    suspend fun addBankBalance(amount: Double)

    /**
     * Removes the specified [amount] from the player's bank balance.
     *
     * @param amount The amount to remove from the bank balance.
     */
    suspend fun removeBankBalance(amount: Double)

    /**
     * Checks if the player has a sufficient bank balance for the given [amount].
     *
     * @param amount The amount to check against the player's bank balance.
     * @return `true` if the player has enough bank balance, `false` otherwise.
     */
    suspend fun hasBankBalance(amount: Double): Boolean

    /**
     * Retrieves the bank balance history.
     *
     * @param limit The maximum number of transactions to retrieve. Defaults to 10.
     * @return A list of [RpTransaction] representing the bank balance history.
     */
    suspend fun getBankBalanceHistory(limit: Int = 10): List<RpTransaction>

    /**
     * Retrieves the player's crypto balance.
     *
     * @return The crypto balance as a [Double].
     */
    suspend fun getCryptoBalance(): Double

    /**
     * Adds the specified [amount] to the player's crypto balance.
     *
     * @param amount The amount to add to the crypto balance.
     */
    suspend fun addCryptoBalance(amount: Double)

    /**
     * Removes the specified [amount] from the player's crypto balance.
     *
     * @param amount The amount to remove from the crypto balance.
     */
    suspend fun removeCryptoBalance(amount: Double)

    /**
     * Checks if the player has a sufficient crypto balance for the given [amount].
     *
     * @param amount The amount to check against the player's crypto balance.
     * @return `true` if the player has enough crypto balance, `false` otherwise.
     */
    suspend fun hasCryptoBalance(amount: Double): Boolean

    /**
     * Retrieves the crypto balance history.
     *
     * @param limit The maximum number of transactions to retrieve. Defaults to 10.
     * @return A list of [RpTransaction] representing the crypto balance history.
     */
    suspend fun getCryptoBalanceHistory(limit: Int = 10): List<RpTransaction>
}