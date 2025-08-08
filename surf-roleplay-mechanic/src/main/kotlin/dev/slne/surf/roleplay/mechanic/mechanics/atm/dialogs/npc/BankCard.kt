package dev.slne.surf.roleplay.mechanic.mechanics.atm.dialogs.npc

import dev.slne.surf.roleplay.api.player.RpPlayer
import dev.slne.surf.surfapi.bukkit.api.builder.ItemStack
import dev.slne.surf.surfapi.bukkit.api.builder.buildLore
import dev.slne.surf.surfapi.bukkit.api.builder.displayName
import dev.slne.surf.surfapi.core.api.font.toSmallCaps
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.persistence.PersistentDataType
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object BankCard {

    internal val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    val bankCardKey = NamespacedKey("surf-roleplay-mechanic", "bank_card")

    fun bankCard(player: RpPlayer) = ItemStack(Material.FEATHER) {
        displayName {
            primary("EC Karte")
        }
        buildLore {
            line {
                spacer(" - ")
                variableKey("Besitzer".toSmallCaps())
                variableValue(player.information.firstName.toString())
            }
            line {
                spacer(" - ")
                variableKey("Beantragt am".toSmallCaps())
                variableValue(formatter.format(LocalDate.now()))
            }
        }
        editPersistentDataContainer {
            it.set(bankCardKey, PersistentDataType.BYTE, 1.toByte())
        }
    }
}