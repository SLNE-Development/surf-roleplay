package dev.slne.roleplay.mod.common.network.utils

import dev.slne.roleplay.mod.common.network.SurfRoleplayPacket
import dev.slne.roleplay.mod.common.network.packets.RoleplayPacket
import dev.slne.surf.surfapi.core.api.util.mutableObject2ObjectMapOf
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation

private val metaCache =
    mutableObject2ObjectMapOf<KClass<out RoleplayPacket>, SurfRoleplayPacket>(512)

fun KClass<out RoleplayPacket>.getPacketMeta() = getPacketMetaOrNull()
    ?: error("NettyPacket class '$qualifiedName' must be annotated with @${SurfRoleplayPacket::class.simpleName}")

fun KClass<out RoleplayPacket>.getPacketMetaOrNull() =
    metaCache[this] ?: findAnnotation<SurfRoleplayPacket>()?.also { metaCache[this] = it }
