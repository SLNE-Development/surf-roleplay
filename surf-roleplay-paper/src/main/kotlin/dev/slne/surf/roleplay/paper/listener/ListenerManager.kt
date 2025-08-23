@file:Suppress("UnstableApiUsage")

package dev.slne.surf.roleplay.paper.listener

import com.github.retrooper.packetevents.event.PacketListener
import com.github.retrooper.packetevents.event.PacketSendEvent
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes
import com.github.retrooper.packetevents.protocol.packettype.PacketType
import com.github.retrooper.packetevents.protocol.sound.Sounds
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSoundEffect
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSpawnEntity
import com.github.shynixn.mccoroutine.folia.entityDispatcher
import com.github.shynixn.mccoroutine.folia.launch
import com.github.shynixn.mccoroutine.folia.regionDispatcher
import dev.jorel.commandapi.kotlindsl.commandAPICommand
import dev.jorel.commandapi.kotlindsl.playerExecutor
import dev.slne.surf.roleplay.paper.spring.RpPacketListener
import dev.slne.surf.roleplay.paper.plugin
import dev.slne.surf.surfapi.bukkit.api.builder.ItemStack
import dev.slne.surf.surfapi.core.api.messages.adventure.playSound
import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.Consumable
import io.papermc.paper.datacomponent.item.UseCooldown
import kotlinx.coroutines.withContext
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.enchantments.Enchantment
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.event.entity.ProjectileLaunchEvent
import org.springframework.stereotype.Component

@Component
class ListenerManager : Listener {

    fun registerListeners() {
        commandAPICommand("give-bow") {
            playerExecutor { player, arguments ->
                player.inventory.addItem(ItemStack(Material.BOW) {
                    val consumable = Consumable.consumable()
                        .consumeSeconds(0.1f)
                        .build()

                    setData(DataComponentTypes.CONSUMABLE, consumable)
                })

                player.inventory.addItem(ItemStack(Material.WIND_CHARGE) {
                    val cooldown = UseCooldown.useCooldown(Float.MIN_VALUE).build()

                    setData(DataComponentTypes.USE_COOLDOWN, cooldown)
                })

                player.inventory.addItem(ItemStack(Material.CROSSBOW) {
                    addUnsafeEnchantment(Enchantment.QUICK_CHARGE, 255)
                })
            }
        }
    }

    @RpPacketListener
    class ArrowHitListener : PacketListener {
        override fun onPacketSend(event: PacketSendEvent) {
            if (event.packetType == PacketType.Play.Server.SPAWN_ENTITY) {
                val packet = WrapperPlayServerSpawnEntity(event)
                val type = packet.entityType

                if (type == EntityTypes.ARROW) {
                    event.isCancelled = true
                }
            }

            if (event.packetType == PacketType.Play.Server.SOUND_EFFECT) {
                val packet = WrapperPlayServerSoundEffect(event)
                val sound = packet.sound

                val blockSounds = setOf(
                    Sounds.ENTITY_ARROW_HIT,
                    Sounds.ENTITY_ARROW_SHOOT,
                    Sounds.ENTITY_ARROW_SHOOT
                )

                if (sound in blockSounds) {
                    event.isCancelled = true
                }
            }
        }
    }

    @EventHandler
    fun onProjectileLaunch(event: ProjectileLaunchEvent) {
        val projectile = event.entity
        val speed = 20

        projectile.setGravity(false)
        projectile.velocity = projectile.velocity.normalize().multiply(speed)
    }

    @EventHandler
    fun onPlayerLaunchProjectile(event: ProjectileHitEvent) {
        val projectile = event.entity
        val projectileLocation = projectile.location

        val hitBlock = event.hitBlock
        val hitEntity = event.hitEntity
        val location = hitBlock?.location ?: hitEntity?.location ?: projectileLocation

        val particleColor = if (hitBlock != null) {
            hitBlock.blockData.mapColor
        } else if (hitEntity != null) {
            Color.RED
        } else {
            Color.GRAY
        }

        val players = projectile.world.getNearbyPlayers(location, 50.0)

        projectile.remove()

        plugin.launch {
            for (player in players) {
                withContext(plugin.regionDispatcher(location)) {
                    player.spawnParticle(
                        Particle.DUST,
                        projectileLocation,
                        10,
                        0.0,
                        0.0,
                        0.0,
                        0.0,
                        Particle.DustOptions(particleColor, 1.5f)
                    )
                }

                if (hitBlock != null) {
                    withContext(plugin.entityDispatcher(player)) {
                        player.playSound {
                            type(Sound.BLOCK_POINTED_DRIPSTONE_LAND)
                            volume(0.25f)
                        }
                    }
                }
            }
        }
    }

}