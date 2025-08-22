package dev.slne.surf.roleplay.core.common.transaction.utils

import dev.slne.surf.roleplay.core.common.player.identity.RpIdentity
import dev.slne.surf.roleplay.core.common.player.identity.identities.CivilianIdentity
import dev.slne.surf.roleplay.core.common.player.identity.identities.PoliceIdentity
import dev.slne.surf.roleplay.core.common.player.identity.identities.RescueServiceIdentity

enum class BalanceType(val currencyName: (RpIdentity) -> String) {
    CASH({
        when (it) {
            is CivilianIdentity -> "civilian_cash"
            is PoliceIdentity -> "police_cash"
            is RescueServiceIdentity -> "rescue_service_cash"
            else -> error("Unknown identity type: ${it::class.simpleName}")
        }
    }),

    BANK({
        when (it) {
            is CivilianIdentity -> "civilian_bank"
            is PoliceIdentity -> "police_bank"
            is RescueServiceIdentity -> "rescue_service_bank"
            else -> error("Unknown identity type: ${it::class.simpleName}")
        }
    }),

    CRYPTO({
        when (it) {
            is CivilianIdentity -> "civilian_crypto"
            is PoliceIdentity -> "police_crypto"
            is RescueServiceIdentity -> "rescue_service_crypto"
            else -> error("Unknown identity type: ${it::class.simpleName}")
        }
    });

    fun getCurrencyName(identity: RpIdentity) =
        currencyName(identity).let { "roleplay_$it" }
}