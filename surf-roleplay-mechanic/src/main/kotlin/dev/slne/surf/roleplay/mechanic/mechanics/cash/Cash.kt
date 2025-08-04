package dev.slne.surf.roleplay.mechanic.mechanics.cash

import com.github.shynixn.mccoroutine.folia.launch
import com.github.shynixn.mccoroutine.folia.regionDispatcher
import dev.slne.surf.roleplay.api.mechanic.cash.utils.AddCashToInventoryResult
import dev.slne.surf.roleplay.api.mechanic.cash.utils.CashCalculator
import dev.slne.surf.roleplay.api.mechanic.cash.utils.CashInInventoryResult
import dev.slne.surf.roleplay.api.mechanic.cash.utils.RemoveCashFromInventoryResult
import dev.slne.surf.roleplay.api.player.RpPlayer
import dev.slne.surf.roleplay.mechanic.mechanics.cash.Cash.Keys.CASH_100_KEY
import dev.slne.surf.roleplay.mechanic.mechanics.cash.Cash.Keys.CASH_10_KEY
import dev.slne.surf.roleplay.mechanic.mechanics.cash.Cash.Keys.CASH_1_KEY
import dev.slne.surf.roleplay.mechanic.mechanics.cash.Cash.Keys.CASH_200_KEY
import dev.slne.surf.roleplay.mechanic.mechanics.cash.Cash.Keys.CASH_20_KEY
import dev.slne.surf.roleplay.mechanic.mechanics.cash.Cash.Keys.CASH_2_KEY
import dev.slne.surf.roleplay.mechanic.mechanics.cash.Cash.Keys.CASH_500_KEY
import dev.slne.surf.roleplay.mechanic.mechanics.cash.Cash.Keys.CASH_50_KEY
import dev.slne.surf.roleplay.mechanic.mechanics.cash.Cash.Keys.CASH_5_KEY
import dev.slne.surf.roleplay.mechanic.plugin
import dev.slne.surf.surfapi.bukkit.api.builder.ItemStack
import dev.slne.surf.surfapi.bukkit.api.builder.buildLore
import dev.slne.surf.surfapi.bukkit.api.builder.displayName
import dev.slne.surf.surfapi.core.api.util.mutableObjectListOf
import dev.slne.surf.surfapi.core.api.util.objectSetOf
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

private fun getKeyStringAndColor(key: NamespacedKey): Pair<String, TextColor> {
    return when (key) {
        CASH_1_KEY -> "1€" to TextColor.color(0xBEC2CB)
        CASH_2_KEY -> "2€" to TextColor.color(0xFFD700)
        CASH_5_KEY -> "5€" to TextColor.color(0x687c73)
        CASH_10_KEY -> "10€" to TextColor.color(0x953e3c)
        CASH_20_KEY -> "20€" to TextColor.color(0x284d71)
        CASH_50_KEY -> "50€" to TextColor.color(0xe49970)
        CASH_100_KEY -> "100€" to TextColor.color(0x99be8a)
        CASH_200_KEY -> "200€" to TextColor.color(0xe8c859)
        CASH_500_KEY -> "500€" to TextColor.color(0xb793bc)
        else -> "?€" to TextColor.color(0xFFFFFF)
    }
}

private fun buildCashItem(key: NamespacedKey): ItemStack {
    return ItemStack(Material.PAPER) {
        val (amountString, color) = getKeyStringAndColor(key)

        displayName {
            text(amountString, color)
        }

        buildLore {
            emptyLine()
            line {
                when (key) {
                    CASH_1_KEY, CASH_2_KEY -> spacer("Ein Münzstück im Wert von $amountString")
                    else -> spacer("Ein Geldschein im Wert von $amountString")
                }
            }
        }

        editPersistentDataContainer { pdc ->
            pdc.set(key, PersistentDataType.BOOLEAN, true)
        }
    }
}

sealed class Cash(val item: ItemStack) {

    object Keys {
        val CASH_1_KEY by lazy { NamespacedKey("roleplay", "cash_1") }
        val CASH_2_KEY by lazy { NamespacedKey("roleplay", "cash_2") }
        val CASH_5_KEY by lazy { NamespacedKey("roleplay", "cash_5") }
        val CASH_10_KEY by lazy { NamespacedKey("roleplay", "cash_10") }
        val CASH_20_KEY by lazy { NamespacedKey("roleplay", "cash_20") }
        val CASH_50_KEY by lazy { NamespacedKey("roleplay", "cash_50") }
        val CASH_100_KEY by lazy { NamespacedKey("roleplay", "cash_100") }
        val CASH_200_KEY by lazy { NamespacedKey("roleplay", "cash_200") }
        val CASH_500_KEY by lazy { NamespacedKey("roleplay", "cash_500") }
    }

    object Cash1 : Cash(buildCashItem(CASH_1_KEY))
    object Cash2 : Cash(buildCashItem(CASH_2_KEY))
    object Cash5 : Cash(buildCashItem(CASH_5_KEY))
    object Cash10 : Cash(buildCashItem(CASH_10_KEY))
    object Cash20 : Cash(buildCashItem(CASH_20_KEY))
    object Cash50 : Cash(buildCashItem(CASH_50_KEY))
    object Cash100 : Cash(buildCashItem(CASH_100_KEY))
    object Cash200 : Cash(buildCashItem(CASH_200_KEY))
    object Cash500 : Cash(buildCashItem(CASH_500_KEY))

    companion object {
        fun removeFromPlayerInventory(
            player: RpPlayer,
            amount: Int
        ): RemoveCashFromInventoryResult {
            val currentBalance = getCashBalanceFromInventory(player)

            if (currentBalance is CashInInventoryResult.PlayerNotOnline) {
                return RemoveCashFromInventoryResult.PLAYER_NOT_ONLINE
            }

            if (currentBalance !is CashInInventoryResult.Success) {
                return RemoveCashFromInventoryResult.UNKNOWN
            }

            var (amount1, amount2, amount5, amount10, amount20, amount50, amount100, amount200, amount500) = currentBalance
            val totalBalance = currentBalance.totalAmount

            if (totalBalance < amount) {
                return RemoveCashFromInventoryResult.NOT_ENOUGH_CASH
            }

            val toRemove = mutableObjectListOf<Cash>()
            var remainingAmount = amount

            if (remainingAmount <= 0) return RemoveCashFromInventoryResult.SUCCESS

            while (remainingAmount > 0) {
                when {
                    remainingAmount >= 500 && amount500 > 0 -> {
                        toRemove.add(Cash500)
                        amount500--
                        remainingAmount -= 500
                    }

                    remainingAmount >= 200 && amount200 > 0 -> {
                        toRemove.add(Cash200)
                        amount200--
                        remainingAmount -= 200
                    }

                    remainingAmount >= 100 && amount100 > 0 -> {
                        toRemove.add(Cash100)
                        amount100--
                        remainingAmount -= 100
                    }

                    remainingAmount >= 50 && amount50 > 0 -> {
                        toRemove.add(Cash50)
                        amount50--
                        remainingAmount -= 50
                    }

                    remainingAmount >= 20 && amount20 > 0 -> {
                        toRemove.add(Cash20)
                        amount20--
                        remainingAmount -= 20
                    }

                    remainingAmount >= 10 && amount10 > 0 -> {
                        toRemove.add(Cash10)
                        amount10--
                        remainingAmount -= 10
                    }

                    remainingAmount >= 5 && amount5 > 0 -> {
                        toRemove.add(Cash5)
                        amount5--
                        remainingAmount -= 5
                    }

                    remainingAmount >= 2 && amount2 > 0 -> {
                        toRemove.add(Cash2)
                        amount2--
                        remainingAmount -= 2
                    }

                    amount1 > 0 -> {
                        toRemove.add(Cash1)
                        amount1--
                        remainingAmount -= 1
                    }

                    else -> break
                }
            }

            if (remainingAmount > 0) {
                return RemoveCashFromInventoryResult.NOT_ENOUGH_CASH
            }

            val bukkitPlayer = player.bukkitPlayer
                ?: return RemoveCashFromInventoryResult.PLAYER_NOT_ONLINE

            toRemove.forEach { cash ->
                val cashItem = cash.item.clone().apply {
                    this.amount = 1
                }
                bukkitPlayer.inventory.removeItem(cashItem)
            }

            return RemoveCashFromInventoryResult.SUCCESS
        }

        fun dropAndRemoveCash(player: RpPlayer) {
            val bukkitPlayer = player.bukkitPlayer ?: return

            val cashBalance = getCashBalanceFromInventory(player)
            if (cashBalance !is CashInInventoryResult.Success) {
                return
            }

            val location = bukkitPlayer.location

            dropItem(location, Cash1.item, cashBalance.amount1)
            dropItem(location, Cash2.item, cashBalance.amount2)
            dropItem(location, Cash5.item, cashBalance.amount5)
            dropItem(location, Cash10.item, cashBalance.amount10)
            dropItem(location, Cash20.item, cashBalance.amount20)
            dropItem(location, Cash50.item, cashBalance.amount50)
            dropItem(location, Cash100.item, cashBalance.amount100)
            dropItem(location, Cash200.item, cashBalance.amount200)
            dropItem(location, Cash500.item, cashBalance.amount500)

            removeFromPlayerInventory(player, cashBalance.totalAmount)
        }

        private fun dropItem(location: Location, item: ItemStack, amount: Int) {
            for (i in 1..amount) {
                location.world.dropItem(location, item.clone())
            }
        }

        fun getCashBalanceFromInventory(player: RpPlayer): CashInInventoryResult {
            val bukkitPlayer = player.bukkitPlayer
                ?: return CashInInventoryResult.PlayerNotOnline

            val cashKeys = listOf(
                CASH_1_KEY, CASH_2_KEY, CASH_5_KEY, CASH_10_KEY,
                CASH_20_KEY, CASH_50_KEY, CASH_100_KEY, CASH_200_KEY, CASH_500_KEY
            )
            val cashAmounts = cashKeys.associateWith { key ->
                getCashAmountFromInventory(bukkitPlayer, key)
            }

            return CashInInventoryResult.Success(
                amount1 = cashAmounts[CASH_1_KEY] ?: 0,
                amount2 = cashAmounts[CASH_2_KEY] ?: 0,
                amount5 = cashAmounts[CASH_5_KEY] ?: 0,
                amount10 = cashAmounts[CASH_10_KEY] ?: 0,
                amount20 = cashAmounts[CASH_20_KEY] ?: 0,
                amount50 = cashAmounts[CASH_50_KEY] ?: 0,
                amount100 = cashAmounts[CASH_100_KEY] ?: 0,
                amount200 = cashAmounts[CASH_200_KEY] ?: 0,
                amount500 = cashAmounts[CASH_500_KEY] ?: 0
            )
        }

        private fun getCashAmountFromInventory(
            player: Player,
            key: NamespacedKey
        ) = player.inventory
            .mapNotNull { it }
            .filter { it.persistentDataContainer.has(key) }
            .sumOf { it.amount }

        fun addToPlayerInventory(player: RpPlayer, amount: Int): AddCashToInventoryResult {
            val bukkitPlayer = player.bukkitPlayer
                ?: return AddCashToInventoryResult.PLAYER_NOT_ONLINE
            val calculatorResult = CashCalculator.calculateCash(amount)

            if (calculatorResult is CashCalculator.CashCalculatorResult.NotCalculable) {
                return AddCashToInventoryResult.CASH_NOT_CALCULABLE
            }

            if (calculatorResult is CashCalculator.CashCalculatorResult.Success) {
                val items = objectSetOf(
                    Cash1 to calculatorResult.amount1,
                    Cash2 to calculatorResult.amount2,
                    Cash5 to calculatorResult.amount5,
                    Cash10 to calculatorResult.amount10,
                    Cash20 to calculatorResult.amount20,
                    Cash50 to calculatorResult.amount50,
                    Cash100 to calculatorResult.amount100,
                    Cash200 to calculatorResult.amount200,
                    Cash500 to calculatorResult.amount500
                )

                items.forEach { item ->
                    val (cash, amount) = item

                    if (amount > 0) {
                        addOrDropToInventory(bukkitPlayer, cash, amount)
                    }
                }
            }

            return AddCashToInventoryResult.SUCCESS
        }

        private fun addOrDropToInventory(player: Player, cash: Cash, amount: Int) {
            if (amount <= 0) return

            val prototype = cash.item.clone()
            val maxStackSize = prototype.maxStackSize

            var remaining = amount

            while (remaining > 0) {
                val toAdd = minOf(remaining, maxStackSize)
                val stack = prototype.clone().also { it.amount = toAdd }

                val leftoverMap = player.inventory.addItem(stack)

                if (leftoverMap.isNotEmpty()) {
                    leftoverMap.values.forEach { leftoverStack ->
                        plugin.launch(plugin.regionDispatcher(player.location)) {
                            player.world.dropItemNaturally(player.location, leftoverStack) { item ->
                                item.owner = player.uniqueId
                                item.isUnlimitedLifetime = true
                            }
                        }
                    }
                }

                remaining -= toAdd
            }
        }
    }
}