package dev.slne.surf.roleplay.paper.command.args

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.arguments.Argument
import dev.jorel.commandapi.arguments.ArgumentSuggestions
import dev.jorel.commandapi.arguments.CustomArgument
import dev.jorel.commandapi.arguments.StringArgument
import dev.slne.surf.roleplay.api.common.transaction.utils.BalanceType

class BalanceTypeArgument(nodeName: String) : CustomArgument<BalanceType, String>(
    StringArgument(nodeName),
    { info ->
        BalanceType.valueOf(info.input.uppercase())
    }
) {
    init {
        replaceSuggestions(ArgumentSuggestions.stringCollection {
            BalanceType.entries.map { it.name.lowercase() }
        })
    }
}

inline fun CommandAPICommand.balanceTypeArgument(
    nodeName: String,
    optional: Boolean = false,
    block: Argument<*>.() -> Unit = {}
) = withArguments(BalanceTypeArgument(nodeName).apply {
    isOptional = optional
    block()
})