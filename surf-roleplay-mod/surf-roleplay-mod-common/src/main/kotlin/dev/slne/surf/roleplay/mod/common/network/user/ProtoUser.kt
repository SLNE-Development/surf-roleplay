package dev.slne.surf.roleplay.mod.common.network.user

import me.mrnavastar.protoweaver.api.netty.ProtoConnection
import java.util.*

abstract class ProtoUser(
    val uuid: UUID,
) {
    abstract val connection: ProtoConnection

}