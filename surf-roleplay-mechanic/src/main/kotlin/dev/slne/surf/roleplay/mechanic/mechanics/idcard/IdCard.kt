package dev.slne.surf.roleplay.mechanic.mechanics.idcard

import com.jeff_media.morepersistentdatatypes.DataType
import dev.slne.surf.surfapi.bukkit.api.builder.ItemStack
import dev.slne.surf.surfapi.bukkit.api.builder.buildLore
import dev.slne.surf.surfapi.bukkit.api.builder.displayName
import dev.slne.surf.surfapi.core.api.font.toSmallCaps
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataType
import java.time.LocalDate
import java.time.format.DateTimeFormatter


object IdCard {

    internal val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    val idCardKey = NamespacedKey("surf-roleplay-mechanic", "id_card")
    val idCardOwnerKey = NamespacedKey("surf-roleplay-mechanic", "id_card_owner")

    fun idCard(firstName: String, lastName: String, birthDate: LocalDate, player: Player) =
        ItemStack(Material.FEATHER) {
            displayName {
                primary("Personalausweis")
            }
            buildLore {
                line {
                    spacer(" - ")
                    variableKey("Vorname: ".toSmallCaps())
                    variableValue(firstName)
                }
                line {
                    spacer(" - ")
                    variableKey("Nachname: ".toSmallCaps())
                    variableValue(lastName)
                }
                line {
                    spacer(" - ")
                    variableKey("Geburtsdatum: ".toSmallCaps())
                    variableValue(formatter.format(birthDate))
                }
            }
            editPersistentDataContainer {
                it.set(idCardKey, PersistentDataType.BYTE, 1.toByte())
                it.set(idCardOwnerKey, DataType.UUID, player.uniqueId)
            }
        }
}