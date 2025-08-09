package dev.slne.surf.roleplay.mechanic.mechanics.atm

import com.jeff_media.morepersistentdatatypes.DataType
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
import java.util.*

object BankCard {

    internal val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    internal val bankCardKey = NamespacedKey("surf-roleplay-mechanic", "bank_card")
    internal val bankCardOwnerKey = NamespacedKey("surf-roleplay-mechanic", "bank_card_owner")
    internal val bankCardActiveKey = NamespacedKey("surf-roleplay-mechanic", "bank_card_active")

    fun bankCard(player: RpPlayer, creationDate: LocalDate, cardId: UUID) = ItemStack(Material.BRICK) {
        displayName {
            primary("Bankkarte")
        }
        buildLore {
            line {
                spacer(" - ")
                variableKey("Besitzer: ".toSmallCaps())
                append(player)
            }
            line {
                spacer(" - ")
                variableKey("Konto erstellt am: ".toSmallCaps())
                variableValue(formatter.format(creationDate))
            }
        }
        editPersistentDataContainer {
            it.set(bankCardKey, PersistentDataType.BYTE, 1.toByte())
            it.set(bankCardOwnerKey, DataType.UUID, player.uuid)
            it.set(bankCardActiveKey, DataType.UUID, cardId)
        }
    }
}