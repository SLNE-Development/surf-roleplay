package dev.slne.surf.roleplay.api.transaction

import dev.slne.surf.roleplay.api.player.utils.BalanceType
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet

interface HasTransactions {

    /**
     * Returns the player's balance for the specified [BalanceType].
     *
     * @param balanceType The type of balance to retrieve.
     * @return The balance amount as a [Int].
     */
    suspend fun getBalance(balanceType: BalanceType): Int

    /**
     * Adds the specified [amount] to the player's balance of the given [BalanceType].
     *
     * @param balanceType The type of balance to add to.
     * @param amount The amount to add to the balance.
     * @return `true` if the operation was successful, `false` otherwise.
     */
    suspend fun addBalance(balanceType: BalanceType, amount: Int): Boolean

    /**
     * Removes the specified [amount] from the player's balance of the given [BalanceType].
     *
     * @param balanceType The type of balance to remove from.
     * @param amount The amount to remove from the balance.
     * @return `true` if the operation was successful, `false` otherwise.
     */
    suspend fun removeBalance(balanceType: BalanceType, amount: Int): Boolean

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
    ): ObjectLinkedOpenHashSet<RpTransaction>

    /**
     * Checks if the player has a sufficient balance of the specified [BalanceType] for the given [amount].
     *
     * @param balanceType The type of balance to check.
     * @param amount The amount to check against the player's balance.
     * @return `true` if the player has enough balance, `false` otherwise.
     */
    suspend fun hasBalance(
        balanceType: BalanceType,
        amount: Int
    ): Boolean = getBalance(balanceType) >= amount

    /**
     * Retrieves the player's cash balance.
     *
     * @return The cash balance as a [Int].
     */
    suspend fun getCashBalance(): Int = getBalance(BalanceType.CASH)

    /**
     * Adds the specified [amount] to the player's cash balance.
     *
     * @param amount The amount to add to the cash balance.
     * @return `true` if the operation was successful, `false` otherwise.
     */
    suspend fun addCashBalance(amount: Int) = addBalance(BalanceType.CASH, amount)

    /**
     * Removes the specified [amount] from the player's cash balance.
     *
     * @param amount The amount to remove from the cash balance.
     * @return `true` if the operation was successful, `false` otherwise.
     */
    suspend fun removeCashBalance(amount: Int) = removeBalance(BalanceType.CASH, amount)

    /**
     * Checks if the player has a sufficient cash balance for the given [amount].
     *
     * @param amount The amount to check against the player's cash balance.
     * @return `true` if the player has enough cash balance, `false` otherwise.
     */
    suspend fun hasCashBalance(amount: Int): Boolean = hasBalance(BalanceType.CASH, amount)

    /**
     * Retrieves the cash balance history.
     *
     * @param limit The maximum number of transactions to retrieve. Defaults to 10.
     * @return A list of [RpTransaction] representing the cash balance history.
     */
    suspend fun getCashBalanceHistory(limit: Int = 10): ObjectLinkedOpenHashSet<RpTransaction> =
        getBalanceHistory(BalanceType.CASH, limit)

    /**
     * Retrieves the player's bank balance.
     *
     * @return The bank balance as a [Int].
     */
    suspend fun getBankBalance(): Int = getBalance(BalanceType.BANK)

    /**
     * Adds the specified [amount] to the player's bank balance.
     *
     * @param amount The amount to add to the bank balance.
     * @return `true` if the operation was successful, `false` otherwise.
     */
    suspend fun addBankBalance(amount: Int) = addBalance(BalanceType.BANK, amount)

    /**
     * Removes the specified [amount] from the player's bank balance.
     *
     * @param amount The amount to remove from the bank balance.
     * @return `true` if the operation was successful, `false` otherwise.
     */
    suspend fun removeBankBalance(amount: Int) = removeBalance(BalanceType.BANK, amount)

    /**
     * Checks if the player has a sufficient bank balance for the given [amount].
     *
     * @param amount The amount to check against the player's bank balance.
     * @return `true` if the player has enough bank balance, `false` otherwise.
     */
    suspend fun hasBankBalance(amount: Int): Boolean = hasBalance(BalanceType.BANK, amount)

    /**
     * Retrieves the bank balance history.
     *
     * @param limit The maximum number of transactions to retrieve. Defaults to 10.
     * @return A list of [RpTransaction] representing the bank balance history.
     */
    suspend fun getBankBalanceHistory(limit: Int = 10): ObjectLinkedOpenHashSet<RpTransaction> =
        getBalanceHistory(BalanceType.BANK, limit)

    /**
     * Retrieves the player's crypto balance.
     *
     * @return The crypto balance as a [Int].
     */
    suspend fun getCryptoBalance(): Int = getBalance(BalanceType.CRYPTO)

    /**
     * Adds the specified [amount] to the player's crypto balance.
     *
     * @param amount The amount to add to the crypto balance.
     * @return `true` if the operation was successful, `false` otherwise.
     */
    suspend fun addCryptoBalance(amount: Int) = addBalance(BalanceType.CRYPTO, amount)

    /**
     * Removes the specified [amount] from the player's crypto balance.
     *
     * @param amount The amount to remove from the crypto balance.
     * @return `true` if the operation was successful, `false` otherwise.
     */
    suspend fun removeCryptoBalance(amount: Int) = removeBalance(BalanceType.CRYPTO, amount)

    /**
     * Checks if the player has a sufficient crypto balance for the given [amount].
     *
     * @param amount The amount to check against the player's crypto balance.
     * @return `true` if the player has enough crypto balance, `false` otherwise.
     */
    suspend fun hasCryptoBalance(amount: Int): Boolean = hasBalance(BalanceType.CRYPTO, amount)

    /**
     * Retrieves the crypto balance history.
     *
     * @param limit The maximum number of transactions to retrieve. Defaults to 10.
     * @return A list of [RpTransaction] representing the crypto balance history.
     */
    suspend fun getCryptoBalanceHistory(limit: Int = 10): ObjectLinkedOpenHashSet<RpTransaction> =
        getBalanceHistory(BalanceType.CRYPTO, limit)
}