package dev.slne.surf.roleplay.mod.common.network.packet.login

import dev.slne.surf.roleplay.mod.common.network.packet.RoleplayPacket
import dev.slne.surf.roleplay.mod.common.network.protocol.listener.login.ServerLoginPacketListener

interface ServerboundRoleplayLoginPacket : RoleplayPacket<ServerLoginPacketListener>