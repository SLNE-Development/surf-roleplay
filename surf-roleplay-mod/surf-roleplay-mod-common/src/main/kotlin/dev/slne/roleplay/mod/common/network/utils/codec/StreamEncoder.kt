package dev.slne.roleplay.mod.common.network.utils.codec

@FunctionalInterface
fun interface StreamEncoder<O, T> {
    fun encode(buf: O, value: T)
}