package dev.slne.surf.roleplay.mod.server.network.user

import com.google.auto.service.AutoService
import dev.slne.surf.roleplay.mod.common.network.user.ProtoUserManager
import me.mrnavastar.protoweaver.api.netty.ProtoConnection
import java.util.*

@AutoService(ProtoUserManager::class)
class ServerProtoUserManager : ProtoUserManager() {
    override fun createUser(
        uuid: UUID,
        connection: ProtoConnection
    ) = ServerProtoUser(uuid, connection)
}