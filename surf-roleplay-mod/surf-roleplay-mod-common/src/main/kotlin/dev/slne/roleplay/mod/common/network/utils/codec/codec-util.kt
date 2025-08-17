@file:OptIn(ExperimentalContracts::class, ExperimentalStdlibApi::class)

package dev.slne.roleplay.mod.common.network.utils.codec

import com.mojang.datafixers.kinds.App
import com.mojang.datafixers.util.Pair
import com.mojang.serialization.*
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.slne.roleplay.mod.common.network.utils.readUtf
import dev.slne.roleplay.mod.common.network.utils.writeUtf
import dev.slne.surf.cloud.api.common.util.codec.fixedSize
import io.netty.buffer.ByteBuf
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import java.time.Duration
import java.util.*
import java.util.function.Function
import java.util.stream.Stream
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.enums.EnumEntries
import kotlin.enums.enumEntries

fun UUID.toIntArray() = leastMostToIntArray(mostSignificantBits, leastSignificantBits)

private fun leastMostToIntArray(uuidMost: Long, uuidLeast: Long): IntArray {
    return intArrayOf(
        (uuidMost shr 32).toInt(),
        uuidMost.toInt(),
        (uuidLeast shr 32).toInt(),
        uuidLeast.toInt()
    )
}

fun IntArray.toUuid(): UUID {
    return UUID(
        this[0].toLong() shl 32 or (this[1].toLong() and 0xFFFFFFFF),
        this[2].toLong() shl 32 or (this[3].toLong() and 0xFFFFFFFF)
    )
}

@DslMarker
annotation class RecordCodecDsl

fun <T> createRecordCodec(@RecordCodecDsl block: RecordCodecBuilder.Instance<T>.() -> App<RecordCodecBuilder.Mu<T>, T>): Codec<T> {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }

    return RecordCodecBuilder.create(block)
}

fun <T> createRecordMapCodec(@RecordCodecDsl block: RecordCodecBuilder.Instance<T>.() -> App<RecordCodecBuilder.Mu<T>, T>): MapCodec<T> {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }

    return RecordCodecBuilder.mapCodec(block)
}

fun <A, O> MapCodec<A>.getter(getter: O.() -> A): RecordCodecBuilder<O, A> =
    this.forGetter(Function(getter))

@Suppress("UnusedReceiverParameter")
fun <T> RecordCodecBuilder.Instance<T>.int(fieldName: String, getter: T.() -> Int) =
    Codec.INT.fieldOf(fieldName).getter(getter)

@Suppress("UnusedReceiverParameter")
fun <T> RecordCodecBuilder.Instance<T>.bool(fieldName: String, getter: T.() -> Boolean) =
    Codec.BOOL.fieldOf(fieldName).getter(getter)

@Suppress("UnusedReceiverParameter")
fun <T> RecordCodecBuilder.Instance<T>.string(fieldName: String, getter: T.() -> String) =
    Codec.STRING.fieldOf(fieldName).getter(getter)

@Suppress("UnusedReceiverParameter")
fun <T> RecordCodecBuilder.Instance<T>.stringOptionalLenient(
    fieldName: String,
    getter: T.() -> String?
) =
    Codec.STRING.lenientOptionalFieldOf(fieldName).forGetter<T> { Optional.ofNullable(getter(it)) }

@Suppress("UnusedReceiverParameter")
fun <T> RecordCodecBuilder.Instance<T>.float(fieldName: String, getter: T.() -> Float) =
    Codec.FLOAT.fieldOf(fieldName).getter(getter)

@Suppress("UnusedReceiverParameter")
fun <T> RecordCodecBuilder.Instance<T>.double(fieldName: String, getter: T.() -> Double) =
    Codec.DOUBLE.fieldOf(fieldName).getter(getter)

@Suppress("UnusedReceiverParameter")
fun <T> RecordCodecBuilder.Instance<T>.long(fieldName: String, getter: T.() -> Long) =
    Codec.LONG.fieldOf(fieldName).getter(getter)

@Suppress("UnusedReceiverParameter")
fun <T> RecordCodecBuilder.Instance<T>.byte(fieldName: String, getter: T.() -> Byte) =
    Codec.BYTE.fieldOf(fieldName).getter(getter)

@Suppress("UnusedReceiverParameter")
fun <T> RecordCodecBuilder.Instance<T>.short(fieldName: String, getter: T.() -> Short) =
    Codec.SHORT.fieldOf(fieldName).getter(getter)

@Suppress("UnusedReceiverParameter")
inline fun <T, reified E : Enum<E>> RecordCodecBuilder.Instance<T>.enum(
    fieldName: String,
    noinline getter: T.() -> E
) =
    ExtraCodecs.enumCodec(enumEntries<E>()).fieldOf(fieldName).getter(getter)

object ExtraCodecs {
    val JAVA = converter(JavaOps.INSTANCE)
    val UUID_CODEC: Codec<UUID> = Codec.INT_STREAM.comapFlatMap(
        { uuidStream -> fixedSize(uuidStream, 4).map { it.toUuid() } },
        { Arrays.stream(it.toIntArray()) }
    )
    val UUID_STRING_CODEC: Codec<UUID> = Codec.STRING.comapFlatMap(
        {
            runCatching { UUID.fromString(it) }.fold(
                { DataResult.success(it) },
                { DataResult.error { "Invalid UUID: $it" } })
        },
        { it.toString() })
    val UUID_LENIENT_CODEC = Codec.withAlternative(UUID_CODEC, UUID_STRING_CODEC)

    fun <T> converter(ops: DynamicOps<T>): Codec<T> =
        Codec.PASSTHROUGH.xmap({ it.convert(ops).value }, { Dynamic(ops, it as T) })


    fun <T : Enum<T>> enumCodec(entries: EnumEntries<T>) = Codec.STRING.validate { input ->
        if (entries.any { it.name == input })
            DataResult.success(input)
        else DataResult.error { "Invalid enum value: $input" }
    }.xmap({ input -> entries.first { it.name == input } }, { it.name })

    fun <E> orCompressed(uncompressedCodec: Codec<E>, compressedCodec: Codec<E>): Codec<E> {
        return object : Codec<E> {
            override fun <T> encode(
                obj: E,
                dynamicOps: DynamicOps<T>,
                obj2: T
            ): DataResult<T> = if (dynamicOps.compressMaps()) compressedCodec.encode(
                obj,
                dynamicOps,
                obj2
            ) else uncompressedCodec.encode(obj, dynamicOps, obj2)


            override fun <T> decode(
                dynamicOps: DynamicOps<T>,
                obj: T
            ): DataResult<Pair<E, T>> = if (dynamicOps.compressMaps()) compressedCodec.decode(
                dynamicOps,
                obj
            ) else uncompressedCodec.decode(dynamicOps, obj)


            override fun toString(): String {
                return "$uncompressedCodec orCompressed $compressedCodec"
            }
        }
    }

    fun <E> orCompressed(
        uncompressedCodec: MapCodec<E>,
        compressedCodec: MapCodec<E>
    ): MapCodec<E> {
        return object : MapCodec<E>() {
            override fun <T> encode(
                obj: E,
                dynamicOps: DynamicOps<T>,
                recordBuilder: RecordBuilder<T>
            ): RecordBuilder<T> = if (dynamicOps.compressMaps()) compressedCodec.encode(
                obj,
                dynamicOps,
                recordBuilder
            ) else uncompressedCodec.encode(obj, dynamicOps, recordBuilder)


            override fun <T> decode(dynamicOps: DynamicOps<T>, mapLike: MapLike<T>): DataResult<E> =
                if (dynamicOps.compressMaps()) compressedCodec.decode(
                    dynamicOps,
                    mapLike
                ) else uncompressedCodec.decode(dynamicOps, mapLike)


            override fun <T> keys(dynamicOps: DynamicOps<T>): Stream<T> {
                return compressedCodec.keys(dynamicOps)
            }

            override fun toString(): String {
                return "$uncompressedCodec orCompressed $compressedCodec"
            }
        }
    }

    fun <T> nonEmptyList(originalCodec: Codec<List<T>>): Codec<List<T>> = originalCodec.validate {
        if (it.isEmpty()) DataResult.error { "List must have contents" } else DataResult.success(it)
    }

    val DURATION_STREAM_CODEC = streamCodec<ByteBuf, Duration>({ buf, duration ->
        buf.writeLong(duration.toMillis())
    }, { buf ->
        Duration.ofMillis(buf.readLong())
    })

    // region adventure
    val COMPONENT = Codec.STRING.comapFlatMap(
        {
            runCatching { GsonComponentSerializer.gson().deserialize(it) }.fold(
                { DataResult.success(it) },
                { DataResult.error { "Invalid component: $it" } })
        },
        { GsonComponentSerializer.gson().serialize(it) }
    )

    val COMPONENT_STREAM = streamCodec<ByteBuf, Component>({ buf, comp ->
        val str = GsonComponentSerializer.gson().serialize(comp)
        buf.writeUtf(str)
    }, { buf ->
        val str = buf.readUtf()
        GsonComponentSerializer.gson().deserialize(str)
    })

}

fun tryCollapseToString(component: Component): String? {
    if (component is TextComponent) {
        if (component.children().isEmpty() && component.style().isEmpty) {
            return component.content()
        }
    }
    return null
}

class FuzzyCodec<T>(
    private val codecs: List<MapCodec<out T>>,
    private val encoderGetter: (T) -> MapEncoder<out T>
) : MapCodec<T>() {

    @Suppress("UNCHECKED_CAST")
    override fun <S> decode(dynamicOps: DynamicOps<S>, mapLike: MapLike<S>): DataResult<T> {
        for (mapDecoder in this.codecs) {
            val dataResult = mapDecoder.decode(dynamicOps, mapLike)
            if (dataResult.result().isPresent) {
                return dataResult as DataResult<T>
            }
        }

        return DataResult.error { "No matching codec found" }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <S> encode(
        obj: T,
        dynamicOps: DynamicOps<S>,
        recordBuilder: RecordBuilder<S>
    ): RecordBuilder<S> {
        val mapEncoder = encoderGetter(obj) as MapEncoder<T>
        return mapEncoder.encode(obj, dynamicOps, recordBuilder)
    }

    override fun <S> keys(dynamicOps: DynamicOps<S>): Stream<S> = codecs.stream()
        .flatMap { it.keys(dynamicOps) }
        .distinct()


    override fun toString(): String {
        return "FuzzyCodec[" + this.codecs + "]"
    }
}

class StrictEither<T>(
    private val typeFieldName: String,
    private val typed: MapCodec<T>,
    private val fuzzy: MapCodec<T>
) : MapCodec<T>() {
    override fun <O> decode(dynamicOps: DynamicOps<O>, mapLike: MapLike<O>): DataResult<T> {
        return if (mapLike[typeFieldName] != null) typed.decode(
            dynamicOps,
            mapLike
        ) else fuzzy.decode(dynamicOps, mapLike)
    }

    override fun <O> encode(
        obj: T,
        dynamicOps: DynamicOps<O>,
        recordBuilder: RecordBuilder<O>
    ): RecordBuilder<O> {
        return fuzzy.encode(obj, dynamicOps, recordBuilder)
    }

    override fun <T1> keys(dynamicOps: DynamicOps<T1>): Stream<T1> {
        return Stream.concat(
            typed.keys(dynamicOps),
            fuzzy.keys(dynamicOps)
        ).distinct()
    }
}