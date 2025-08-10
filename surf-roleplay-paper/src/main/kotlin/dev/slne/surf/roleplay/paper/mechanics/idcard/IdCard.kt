package dev.slne.surf.roleplay.paper.mechanics.idcard

import dev.slne.surf.surfapi.bukkit.api.builder.ItemStack
import dev.slne.surf.surfapi.bukkit.api.builder.buildLore
import dev.slne.surf.surfapi.bukkit.api.builder.displayName
import dev.slne.surf.surfapi.core.api.font.toSmallCaps
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.persistence.PersistentDataType
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object IdCard {

    internal val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    val idCardKey = NamespacedKey("surf-roleplay-mechanic", "id_card")

    fun idCard(firstName: String, lastName: String, birthDate: LocalDate) =
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
                    variableKey("nachname: ".toSmallCaps())
                    variableValue(lastName)
                }
                line {
                    spacer(" - ")
                    variableKey("geburtsdatum: ".toSmallCaps())
                    variableValue(formatter.format(birthDate))
                }
            }
            editPersistentDataContainer {
                it.set(idCardKey, PersistentDataType.BYTE, 1.toByte())
            }
        }
}