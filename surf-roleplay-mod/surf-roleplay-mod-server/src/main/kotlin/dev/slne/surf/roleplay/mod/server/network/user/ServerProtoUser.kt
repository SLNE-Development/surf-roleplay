package dev.slne.surf.roleplay.mod.server.network.user

import dev.slne.surf.roleplay.mod.common.network.user.ProtoUser
import me.mrnavastar.protoweaver.api.netty.ProtoConnection
import java.util.*

class ServerProtoUser(uuid: UUID, override val connection: ProtoConnection) : ProtoUser(uuid) {
}