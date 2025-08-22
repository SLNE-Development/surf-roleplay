package dev.slne.surf.roleplay.core.common.mechanics.rentable

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.ZonedDateTime
import java.util.concurrent.TimeUnit

@Component
class RentCollectorJob {

    @Scheduled(fixedDelay = 1, timeUnit = TimeUnit.SECONDS)
    suspend fun tick() {
        val now = ZonedDateTime.now()

        RentableMechanic.rentables.forEach { rentable ->
            if (rentable.owner == null) return@forEach

            val lastCollection = rentable.lastRentCollection
            val delay = rentable.rentDuration

            if (lastCollection == null || now.isAfter(lastCollection.plusSeconds(delay.inWholeSeconds))) {
                rentable.collectRent()
            }
        }
    }

}