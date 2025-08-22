package dev.slne.surf.roleplay.paper.command

import com.github.shynixn.mccoroutine.folia.launch
import dev.jorel.commandapi.kotlindsl.*
import dev.slne.surf.roleplay.core.common.transaction.utils.BalanceType
import dev.slne.surf.roleplay.paper.command.args.balanceTypeArgument
import dev.slne.surf.roleplay.paper.player.rpPlayer
import dev.slne.surf.roleplay.paper.plugin
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText
import org.bukkit.entity.Player

fun balanceCommand() = commandAPICommand("balance") {
    subcommand("get") {
        balanceTypeArgument("balanceType")
        entitySelectorArgumentOnePlayer("player")

        playerExecutor { sender, args ->
            val player: Player by args
            val balanceType: BalanceType by args

            plugin.launch {
                val rpPlayer = player.rpPlayer
                val balance = rpPlayer.getBalance(balanceType)

                sender.sendText {
                    appendPrefix()

                    info("Der Kontostand von ")
                    append(rpPlayer.asComponent())
                    info(" beträgt: ")
                    variableValue(balance)
                    info(".")
                }
            }
        }
    }

    subcommand("set") {
        balanceTypeArgument("balanceType")
        entitySelectorArgumentOnePlayer("player")
        integerArgument("amount")

        playerExecutor { sender, args ->
            val player: Player by args
            val balanceType: BalanceType by args
            val amount: Int by args

            plugin.launch {
                val rpPlayer = player.rpPlayer
                val currentAmount = rpPlayer.getBalance(balanceType)

                rpPlayer.removeBalance(balanceType, currentAmount)
                rpPlayer.addBalance(balanceType, amount)

                sender.sendText {
                    appendPrefix()

                    info("Der Kontostand von ")
                    append(rpPlayer.asComponent())
                    info(" wurde auf ")
                    variableValue(amount)
                    info(" gesetzt.")
                }
            }
        }
    }

    subcommand("add") {
        balanceTypeArgument("balanceType")
        entitySelectorArgumentOnePlayer("player")
        integerArgument("amount")

        playerExecutor { sender, args ->
            val player: Player by args
            val balanceType: BalanceType by args
            val amount: Int by args

            plugin.launch {
                val rpPlayer = player.rpPlayer
                rpPlayer.addBalance(balanceType, amount)

                sender.sendText {
                    appendPrefix()

                    info("Der Kontostand von ")
                    append(rpPlayer.asComponent())
                    info(" wurde um ")
                    variableValue(amount)
                    info(" erhöht.")
                }
            }
        }
    }

    subcommand("remove") {
        balanceTypeArgument("balanceType")
        entitySelectorArgumentOnePlayer("player")
        integerArgument("amount")

        playerExecutor { sender, args ->
            val player: Player by args
            val balanceType: BalanceType by args
            val amount: Int by args

            plugin.launch {
                val rpPlayer = player.rpPlayer
                rpPlayer.removeBalance(balanceType, amount)

                sender.sendText {
                    appendPrefix()

                    info("Der Kontostand von ")
                    append(rpPlayer.asComponent())
                    info(" wurde um ")
                    variableValue(amount)
                    info(" verringert.")
                }
            }
        }
    }
}