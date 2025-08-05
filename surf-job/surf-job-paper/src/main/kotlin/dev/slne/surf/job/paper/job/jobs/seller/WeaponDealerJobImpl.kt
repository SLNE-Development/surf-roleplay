package dev.slne.surf.job.paper.job.jobs.seller

import dev.slne.surf.job.api.job.JobCategory
import dev.slne.surf.job.api.job.jobs.seller.WeaponDealerJob
import dev.slne.surf.job.paper.job.JobImpl
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText

object WeaponDealerJobImpl : JobImpl(
    category = JobCategory.SELLER,
    name = "weapon_dealer",
    displayName = buildText { primary("Waffenhändler") },
    description = { },
    rules = { },
    income = 340,
    maxPlayers = 2,
), WeaponDealerJob