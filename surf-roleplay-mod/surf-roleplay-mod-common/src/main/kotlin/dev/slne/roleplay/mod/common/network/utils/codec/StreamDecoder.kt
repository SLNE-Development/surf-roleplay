package dev.slne.roleplay.mod.common.network.utils.codec

@FunctionalInterface
fun interface StreamDecoder<I, T> {
    fun decode(buf: I): T
}