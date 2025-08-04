package dev.slne.surf.roleplay.core.utils

import org.bukkit.entity.Player
import java.text.NumberFormat
import java.util.*

fun Player.formatNumber(number: Number): String = formatNumber(number, locale())
fun formatNumber(number: Number, locale: Locale): String =
    NumberFormat.getNumberInstance(locale).format(number)