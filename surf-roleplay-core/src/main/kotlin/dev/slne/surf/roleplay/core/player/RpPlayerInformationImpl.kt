package dev.slne.surf.roleplay.core.player

import dev.slne.surf.roleplay.api.player.RpPlayerInformation
import java.time.LocalDate

data class RpPlayerInformationImpl(
    override var firstName: String? = null,
    override var lastName: String? = null,
    override var birthDate: LocalDate? = null
) : RpPlayerInformation
