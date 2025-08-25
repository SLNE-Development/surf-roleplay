package dev.slne.surf.roleplay.mod.common.network.protocol.listener

sealed interface PacketListener {

    interface LoginPacketListener : PacketListener
    interface PlayPacketListener : PacketListener

}