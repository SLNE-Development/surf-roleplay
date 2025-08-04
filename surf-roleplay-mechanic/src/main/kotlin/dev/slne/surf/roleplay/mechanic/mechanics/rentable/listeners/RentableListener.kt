package dev.slne.surf.roleplay.mechanic.mechanics.rentable.listeners

import com.github.shynixn.mccoroutine.folia.entityDispatcher
import com.github.shynixn.mccoroutine.folia.launch
import dev.slne.surf.roleplay.api.mechanic.rentable.events.RentableMemberAddEvent
import dev.slne.surf.roleplay.api.mechanic.rentable.events.RentableMemberRemoveEvent
import dev.slne.surf.roleplay.api.mechanic.rentable.events.RentableOwnerChangeEvent
import dev.slne.surf.roleplay.mechanic.plugin
import dev.slne.surf.surfapi.core.api.messages.adventure.playSound
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText
import kotlinx.coroutines.withContext
import org.bukkit.Sound
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

object RentableListener : Listener {

    @EventHandler
    fun onRentableMemberAdd(event: RentableMemberAddEvent) {
        val rentable = event.rentable
        val members = event.rentable.members

        plugin.launch {
            val memberPlayer = event.member.bukkitPlayer

            if (memberPlayer != null) {
                withContext(plugin.entityDispatcher(memberPlayer)) {
                    memberPlayer.sendText {
                        appendPrefix()

                        info("Du wurdest zu der Immobilie ")
                        append(rentable)
                        info(" hinzugefügt.")
                    }

                    memberPlayer.playSound(true) {
                        type(Sound.ENTITY_VILLAGER_CELEBRATE)
                        volume(.5f)
                        source(net.kyori.adventure.sound.Sound.Source.NEUTRAL)
                    }
                }
            }

            members.filterNot { it == event.member }.forEach { member ->
                val memberPlayer = member.bukkitPlayer ?: return@forEach

                withContext(plugin.entityDispatcher(memberPlayer)) {
                    memberPlayer.sendText {
                        appendPrefix()

                        append(event.member)
                        info(" wurde zu der Immobilie ")
                        append(rentable)
                        info(" hinzugefügt.")
                    }

                    memberPlayer.playSound(true) {
                        type(Sound.ENTITY_VILLAGER_CELEBRATE)
                        volume(.5f)
                        source(net.kyori.adventure.sound.Sound.Source.NEUTRAL)
                    }
                }
            }
        }
    }

    @EventHandler
    fun onRentableMemberRemove(event: RentableMemberRemoveEvent) {
        val rentable = event.rentable
        val members = event.rentable.members

        plugin.launch {
            val memberPlayer = event.member.bukkitPlayer
            if (memberPlayer != null) {
                withContext(plugin.entityDispatcher(memberPlayer)) {
                    memberPlayer.sendText {
                        appendPrefix()

                        info("Du wurdest von der Immobilie ")
                        append(rentable)
                        info(" entfernt.")
                    }

                    memberPlayer.playSound(true) {
                        type(Sound.ENTITY_VILLAGER_NO)
                        volume(.5f)
                        source(net.kyori.adventure.sound.Sound.Source.NEUTRAL)
                    }
                }
            }

            members.filterNot { it == event.member }.forEach { member ->
                val memberPlayer = member.bukkitPlayer ?: return@forEach

                withContext(plugin.entityDispatcher(memberPlayer)) {
                    memberPlayer.sendText {
                        appendPrefix()

                        append(event.member)
                        info(" wurde von der Immobilie ")
                        append(rentable)
                        info(" entfernt.")
                    }

                    memberPlayer.playSound(true) {
                        type(Sound.ENTITY_VILLAGER_NO)
                        volume(.5f)
                        source(net.kyori.adventure.sound.Sound.Source.NEUTRAL)
                    }
                }
            }
        }
    }

    @EventHandler
    fun onRentableOwnerChange(event: RentableOwnerChangeEvent) {
        val reason = event.reason
        val rentable = event.rentable

        val newOwner = event.newOwner
        val oldOwner = event.oldOwner

        val newOwnerPlayer = newOwner?.bukkitPlayer
        val oldOwnerPlayer = oldOwner?.bukkitPlayer

        plugin.launch {
            if (newOwner != null && newOwnerPlayer != null) {
                withContext(plugin.entityDispatcher(newOwnerPlayer)) {
                    newOwnerPlayer.sendText {
                        appendPrefix()

                        success("Du bist jetzt der Eigentümer der Immobilie ")
                        append(rentable)
                        success(".")

                        if (reason == RentableOwnerChangeEvent.OwnerChangeReason.RentableBought) {
                            info(" Du hast die Immobilie gemietet.")
                        } else if (reason is RentableOwnerChangeEvent.OwnerChangeReason.OwnerSetNewOwner) {
                            info(" Du wurdest als neuer Eigentümer gesetzt.")
                        }
                    }

                    newOwnerPlayer.playSound(true) {
                        type(Sound.ENTITY_PLAYER_LEVELUP)
                        volume(.5f)
                        source(net.kyori.adventure.sound.Sound.Source.NEUTRAL)
                    }
                }
            }

            if (oldOwner != null && oldOwnerPlayer != null) {
                withContext(plugin.entityDispatcher(oldOwnerPlayer)) {
                    oldOwnerPlayer.sendText {
                        appendPrefix()

                        info("Du bist nicht mehr der Eigentümer der Immobilie ")
                        append(rentable)
                        info(".")

                        if (reason is RentableOwnerChangeEvent.OwnerChangeReason.OwnerSetNewOwner) {
                            error(" Du wurdest als Eigentümer entfernt.")
                        }
                    }

                    oldOwnerPlayer.playSound(true) {
                        type(Sound.ENTITY_VILLAGER_NO)
                        volume(.5f)
                        source(net.kyori.adventure.sound.Sound.Source.NEUTRAL)
                    }
                }
            }
        }
    }

}