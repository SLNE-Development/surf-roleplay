package dev.slne.surf.roleplay.api.player.utils

import dev.slne.surf.roleplay.api.player.identity.RpIdentity

enum class BalanceType(val currencyName: (RpIdentity) -> String) {
    CASH({
        when (it) {
            is RpIdentity.CivilianIdentity -> "civilian_cash"
            is RpIdentity.PoliceIdentity -> "police_cash"
            is RpIdentity.RescueServiceIdentity -> "rescue_service_cash"
            else -> error("Unknown identity type: ${it::class.simpleName}")
        }
    }),

    BANK({
        when (it) {
            is RpIdentity.CivilianIdentity -> "civilian_bank"
            is RpIdentity.PoliceIdentity -> "police_bank"
            is RpIdentity.RescueServiceIdentity -> "rescue_service_bank"
            else -> error("Unknown identity type: ${it::class.simpleName}")
        }
    }),

    CRYPTO({
        when (it) {
            is RpIdentity.CivilianIdentity -> "civilian_crypto"
            is RpIdentity.PoliceIdentity -> "police_crypto"
            is RpIdentity.RescueServiceIdentity -> "rescue_service_crypto"
            else -> error("Unknown identity type: ${it::class.simpleName}")
        }
    });

    fun getCurrencyName(identity: RpIdentity) =
        currencyName(identity).let { "roleplay_$it" }
}