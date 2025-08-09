package dev.slne.surf.roleplay.paper

import com.github.shynixn.mccoroutine.folia.SuspendingJavaPlugin
import com.github.shynixn.mccoroutine.folia.launch
import dev.jorel.commandapi.kotlindsl.*
import dev.slne.surf.roleplay.api.player.rpPlayer
import dev.slne.surf.roleplay.api.player.utils.BalanceType
import dev.slne.surf.roleplay.core.RpCore
import dev.slne.surf.roleplay.core.RpDatabase
import dev.slne.surf.roleplay.core.player.license.licenseServiceImpl
import dev.slne.surf.roleplay.mechanic.mechanicRegistryImpl
import dev.slne.surf.roleplay.paper.command.args.balanceTypeArgument
import dev.slne.surf.roleplay.paper.listeners.ListenerManager
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

val plugin get() = JavaPlugin.getPlugin(SurfRoleplay::class.java)

class SurfRoleplay : SuspendingJavaPlugin() {

    private lateinit var rpDatabase: RpDatabase

    override suspend fun onLoadAsync() {
        RpCore.plugin = this
        rpDatabase = RpDatabase(dataPath)

        mechanicRegistryImpl.registerMechanics(this)
        mechanicRegistryImpl.registerDatabaseTables(rpDatabase)

        licenseServiceImpl.onLoad()

        rpDatabase.onLoad()

        mechanicRegistryImpl.loadMechanics()
    }

    override suspend fun onEnableAsync() {
        ListenerManager.registerListeners()

        mechanicRegistryImpl.enableMechanics()
        mechanicRegistryImpl.registerBukkitHandlers()

        licenseServiceImpl.onEnable()

        commandAPICommand("balance") {
            subcommand("get") {
                balanceTypeArgument("balanceType")
                entitySelectorArgumentOnePlayer("player")

                playerExecutor { sender, args ->
                    val player: Player by args
                    val balanceType: BalanceType by args

                    plugin.launch {
                        val rpPlayer = player.rpPlayer()
                        val balance = rpPlayer.getBalance(balanceType)

                        sender.sendText {
                            appendPrefix()

                            info("Der Kontostand von ")
                            append(rpPlayer)
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
                        val rpPlayer = player.rpPlayer()
                        val currentAmount = rpPlayer.getBalance(balanceType)

                        rpPlayer.removeBalance(balanceType, currentAmount)
                        rpPlayer.addBalance(balanceType, amount)

                        sender.sendText {
                            appendPrefix()

                            info("Der Kontostand von ")
                            append(rpPlayer)
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
                        val rpPlayer = player.rpPlayer()
                        rpPlayer.addBalance(balanceType, amount)

                        sender.sendText {
                            appendPrefix()

                            info("Der Kontostand von ")
                            append(rpPlayer)
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
                        val rpPlayer = player.rpPlayer()
                        rpPlayer.removeBalance(balanceType, amount)

                        sender.sendText {
                            appendPrefix()

                            info("Der Kontostand von ")
                            append(rpPlayer)
                            info(" wurde um ")
                            variableValue(amount)
                            info(" verringert.")
                        }
                    }
                }
            }
        }

        commandAPICommand("active-identity") {
            playerExecutor { player, args ->
                plugin.launch {
                    val rpPlayer = player.rpPlayer()
                    val activeIdentity = rpPlayer.activeIdentity

                    player.sendText {
                        appendPrefix()

                        info("Deine aktive Identität: ")
                        if (activeIdentity != null) {
                            append(activeIdentity.type)
                        } else {
                            variableValue("Keine aktive Identität gesetzt")
                        }
                    }
                }
            }
        }
    }

    override suspend fun onDisableAsync() {
        licenseServiceImpl.onDisable()
        mechanicRegistryImpl.disableMechanics()

        rpDatabase.onDisable()
    }

}