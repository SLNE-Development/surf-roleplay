package dev.slne.surf.roleplay.mechanic.mechanics.cash

import com.github.shynixn.mccoroutine.folia.launch
import dev.jorel.commandapi.kotlindsl.*
import dev.slne.surf.roleplay.api.mechanic.cash.CashMechanic
import dev.slne.surf.roleplay.api.player.RpPlayer
import dev.slne.surf.roleplay.api.player.rpPlayer
import dev.slne.surf.roleplay.mechanic.MechanicImpl
import dev.slne.surf.roleplay.mechanic.mechanics.cash.listeners.CashDropHandler
import dev.slne.surf.roleplay.mechanic.plugin
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText
import dev.slne.surf.surfapi.core.api.util.objectSetOf

object CashMechanicImpl : MechanicImpl(
    name = "CashMechanic",
    handlers = objectSetOf(
        CashDropHandler
    )
), CashMechanic {

    override suspend fun onEnable() {
        commandAPICommand("cash") {
            subcommand("balance") {
                playerExecutor { player, args ->
                    plugin.launch {
                        val rpPlayer = player.rpPlayer()
                        val balance = rpPlayer.getCashBalance()

                        player.sendText {
                            appendPrefix()

                            success("Dein aktueller Kontostand beträgt ")
                            variableValue(balance)
                            success(" €.")
                        }
                    }
                }
            }
            subcommand("remove") {
                integerArgument("amount")
                playerExecutor { player, args ->
                    val amount: Int by args

                    plugin.launch {
                        val rpPlayer = player.rpPlayer()
                        val result = rpPlayer.removeCashBalance(amount.toDouble())

                        if (result) {
                            player.sendText {
                                appendPrefix()

                                success("Dir wurden ")
                                variableValue(amount)
                                success(" € abgezogen.")
                            }
                        } else {
                            player.sendText {
                                appendPrefix()

                                error("Es konnte kein Geld abgezogen werden. Bitte versuche es später erneut.")
                            }
                        }
                    }
                }
            }
            subcommand("give") {
                integerArgument("amount")
                playerExecutor { player, args ->
                    val amount: Int by args

                    plugin.launch {
                        val rpPlayer = player.rpPlayer()
                        val result = rpPlayer.addCashBalance(amount.toDouble())

                        if (result) {
                            player.sendText {
                                appendPrefix()

                                success("Dir wurden ")
                                variableValue(amount)
                                success(" € gegeben.")
                            }
                        } else {
                            player.sendText {
                                appendPrefix()

                                error("Es konnte kein Geld gegeben werden. Bitte versuche es später erneut.")
                            }
                        }
                    }
                }
            }
        }
    }

    override fun addCashBalance(
        player: RpPlayer,
        amount: Int
    ) = Cash.addToPlayerInventory(player, amount)

    override fun removeCashBalance(
        player: RpPlayer,
        amount: Int
    ) = Cash.removeFromPlayerInventory(player, amount)

    override fun getCashBalance(player: RpPlayer) = Cash.getCashBalanceFromInventory(player)
}