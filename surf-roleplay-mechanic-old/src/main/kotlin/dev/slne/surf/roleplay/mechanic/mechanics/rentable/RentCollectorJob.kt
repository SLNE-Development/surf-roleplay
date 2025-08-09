package dev.slne.surf.roleplay.mechanic.mechanics.rentable

import dev.slne.surf.roleplay.api.coroutine.RpJob
import java.time.ZonedDateTime
import kotlin.time.Duration.Companion.seconds

object RentCollectorJob : RpJob("RentCollectorJob", 1.seconds) {

    override suspend fun tick() {
        val now = ZonedDateTime.now()

        RentableMechanicImpl.rentables.forEach { rentable ->
            if (rentable.owner == null) return@forEach

            val lastCollection = rentable.lastRentCollection
            val delay = rentable.rentDuration

            if (lastCollection == null || now.isAfter(lastCollection.plusSeconds(delay.inWholeSeconds))) {
                rentable.collectRent()
            }
        }
    }

}