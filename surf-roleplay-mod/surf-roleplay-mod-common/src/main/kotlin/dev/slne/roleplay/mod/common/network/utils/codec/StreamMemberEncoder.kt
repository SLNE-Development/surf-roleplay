package dev.slne.roleplay.mod.common.network.utils.codec

@FunctionalInterface
fun interface StreamMemberEncoder<O, T> {
    fun encode(value: T, buf: O)
}