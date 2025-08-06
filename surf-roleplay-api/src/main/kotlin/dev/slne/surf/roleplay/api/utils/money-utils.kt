package dev.slne.surf.roleplay.api.utils

import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

private val formatter by lazy {
    DecimalFormatSymbols.getInstance(Locale.GERMAN).apply {
        groupingSeparator = '.'
        decimalSeparator = ','
    }
}

private val decimalFormatter by lazy {
    DecimalFormat("#,##0.00", formatter).apply {
        isGroupingUsed = true
        isDecimalSeparatorAlwaysShown = true
    }
}

fun formatNumber(number: Number) = decimalFormatter.format(number)
fun Number.formatNumber() = decimalFormatter.format(this)

fun formatMoney(amount: Number) = "${formatNumber(amount)} €"
fun Number.formatMoney() = formatMoney(this)

fun formatMoneyComponent(amount: Number) = buildText {
    variableValue(formatMoney(amount))
}

fun Number.formatMoneyComponent() = formatMoneyComponent(this)