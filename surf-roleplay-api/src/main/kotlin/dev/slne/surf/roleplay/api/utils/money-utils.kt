package dev.slne.surf.roleplay.api.utils

import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

private val symbols by lazy {
    DecimalFormatSymbols.getInstance(Locale.GERMAN).apply {
        groupingSeparator = '.'
        decimalSeparator = ','
    }
}

private val decimalFormatter by lazy {
    DecimalFormat("#,##0", symbols).apply {
        isGroupingUsed = true
        isDecimalSeparatorAlwaysShown = false
    }
}

fun Number.formatNumber(): String = decimalFormatter.format(this)

fun Number.formatMoney(): String = "${this.formatNumber()} €"

fun Number.formatMoneyComponent() = buildText {
    variableValue(this@formatMoneyComponent.formatMoney())
}
