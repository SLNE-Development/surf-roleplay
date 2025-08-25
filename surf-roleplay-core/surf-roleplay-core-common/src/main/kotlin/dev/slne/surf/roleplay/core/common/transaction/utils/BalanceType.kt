package dev.slne.surf.roleplay.core.common.transaction.utils

import dev.slne.surf.roleplay.core.common.player.identity.RpIdentityType

enum class BalanceType(private val currencyName: (RpIdentityType) -> String) {
    CASH({
        when (it) {
            RpIdentityType.CIVILIAN -> "civilian_cash"
            RpIdentityType.POLICE -> "police_cash"
            RpIdentityType.RESCUE_SERVICE -> "rescue_service_cash"
        }
    }),

    BANK({
        when (it) {
            RpIdentityType.CIVILIAN -> "civilian_bank"
            RpIdentityType.POLICE -> "police_bank"
            RpIdentityType.RESCUE_SERVICE -> "rescue_service_bank"
        }
    }),

    CRYPTO({
        when (it) {
            RpIdentityType.CIVILIAN -> "civilian_crypto"
            RpIdentityType.POLICE -> "police_crypto"
            RpIdentityType.RESCUE_SERVICE -> "rescue_service_crypto"
        }
    });

    fun getCurrencyName(type: RpIdentityType) = "roleplay_${currencyName(type)}"
}