package dev.slne.surf.job.paper.job.jobs.seller

import dev.slne.surf.job.api.job.jobs.seller.WeaponDealerJob
import dev.slne.surf.job.paper.job.JobImpl
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText

object WeaponDealerJobImpl : JobImpl(
    name = "weapon_dealer",
    displayName = buildText { primary("Waffenhändler") },
    income = 340,
    maxPlayers = 1,
), WeaponDealerJob