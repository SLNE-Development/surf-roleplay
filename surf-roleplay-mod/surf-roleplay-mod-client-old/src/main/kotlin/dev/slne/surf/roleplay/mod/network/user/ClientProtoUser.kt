package dev.slne.surf.roleplay.mod.network.user

import dev.slne.surf.roleplay.mod.RoleplayMod
import dev.slne.surf.roleplay.mod.common.network.user.ProtoUser
import me.mrnavastar.protoweaver.api.netty.ProtoConnection

object ClientProtoUser : ProtoUser(RoleplayMod.uuid) {
    override val connection: ProtoConnection
        get() = RoleplayMod.client?.connection ?: error("Not connected to a roleplay server")
}