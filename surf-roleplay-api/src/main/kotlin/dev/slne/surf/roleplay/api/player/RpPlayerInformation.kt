package dev.slne.surf.roleplay.api.player

import java.time.LocalDate

interface RpPlayerInformation {
    /**
     * The first name of the player.
     */
    var firstName: String?

    /**
     * The last name of the player.
     */
    var lastName: String?

    /**
     * The birth date of the player.
     */
    var birthDate: LocalDate?
}