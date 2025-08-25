package dev.slne.surf.roleplay.paper.mechanics.rentable.listeners

import dev.slne.surf.cloud.api.common.event.CloudEventHandler
import dev.slne.surf.roleplay.paper.mechanics.rentable.events.RentableMemberAddEvent
import dev.slne.surf.roleplay.paper.mechanics.rentable.events.RentableMemberRemoveEvent
import dev.slne.surf.roleplay.paper.mechanics.rentable.events.RentableOwnerChangeEvent
import dev.slne.surf.roleplay.paper.mechanics.rentable.events.RentableOwnerChangeEvent.OwnerChangeReason
import dev.slne.surf.surfapi.core.api.messages.adventure.playSound
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText
import net.kyori.adventure.sound.Sound.Source
import org.bukkit.Sound
import org.springframework.stereotype.Component

@Suppress("unused")
@Component
class RentableListener {

    @CloudEventHandler
    fun onRentableMemberAdd(event: RentableMemberAddEvent) {
        val rentable = event.rentable
        val members = event.rentable.members
        val memberPlayer = event.member.cloudPlayer.player

        if (memberPlayer != null) {
            memberPlayer.sendText {
                appendPrefix()

                info("Du wurdest zu der Immobilie ")
                append(rentable)
                info(" hinzugefügt.")
            }

            memberPlayer.playSound(true) {
                type(Sound.ENTITY_VILLAGER_CELEBRATE)
                volume(0.5f)
                source(Source.NEUTRAL)
            }
        }

        for (member in members) {
            if (member == event.member) continue
            val memberPlayer = member.cloudPlayer.player ?: continue
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
                source(Source.NEUTRAL)
            }
        }
    }

    @CloudEventHandler
    fun onRentableMemberRemove(event: RentableMemberRemoveEvent) {
        val rentable = event.rentable
        val members = event.rentable.members
        val memberPlayer = event.member.cloudPlayer.player

        if (memberPlayer != null) {
            memberPlayer.sendText {
                appendPrefix()

                info("Du wurdest von der Immobilie ")
                append(rentable)
                info(" entfernt.")
            }

            memberPlayer.playSound(true) {
                type(Sound.ENTITY_VILLAGER_NO)
                volume(.5f)
                source(Source.NEUTRAL)
            }
        }

        for (member in members) {
            if (member == event.member) continue
            val memberPlayer = member.cloudPlayer.player ?: continue
            memberPlayer.sendText {
                appendPrefix()

                append(event.member.asComponent())
                info(" wurde von der Immobilie ")
                append(rentable)
                info(" entfernt.")
            }

            memberPlayer.playSound(true) {
                type(Sound.ENTITY_VILLAGER_NO)
                volume(.5f)
                source(Source.NEUTRAL)
            }
        }
    }

    @CloudEventHandler
    fun onRentableOwnerChange(event: RentableOwnerChangeEvent) {
        val reason = event.reason
        val rentable = event.rentable

        val newOwner = event.newOwner
        val oldOwner = event.oldOwner

        val newOwnerPlayer = newOwner?.cloudPlayer?.player
        val oldOwnerPlayer = oldOwner?.cloudPlayer?.player

        if (newOwner != null && newOwnerPlayer != null) {
            newOwnerPlayer.sendText {
                appendPrefix()

                success("Du bist jetzt der Eigentümer der Immobilie ")
                append(rentable)
                success(".")

                if (reason == OwnerChangeReason.RentableBought) {
                    info(" Du hast die Immobilie gemietet.")
                } else if (reason is OwnerChangeReason.OwnerSetNewOwner) {
                    info(" Du wurdest als neuer Eigentümer gesetzt.")
                }
            }

            newOwnerPlayer.playSound(true) {
                type(Sound.ENTITY_PLAYER_LEVELUP)
                volume(.5f)
                source(Source.NEUTRAL)
            }
        }

        if (oldOwner != null && oldOwnerPlayer != null) {
            oldOwnerPlayer.sendText {
                appendPrefix()

                info("Du bist nicht mehr der Eigentümer der Immobilie ")
                append(rentable)
                info(".")

                if (reason is OwnerChangeReason.OwnerSetNewOwner) {
                    error(" Du wurdest als Eigentümer entfernt.")
                }
            }

            oldOwnerPlayer.playSound(true) {
                type(Sound.ENTITY_VILLAGER_NO)
                volume(.5f)
                source(Source.NEUTRAL)
            }
        }
    }
}