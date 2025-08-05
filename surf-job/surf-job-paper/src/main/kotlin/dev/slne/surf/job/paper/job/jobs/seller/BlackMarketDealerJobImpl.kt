package dev.slne.surf.job.paper.job.jobs.seller

import dev.slne.surf.job.api.job.JobCategory
import dev.slne.surf.job.api.job.jobs.seller.BlackMarketDealerJob
import dev.slne.surf.job.paper.job.JobImpl
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText

object BlackMarketDealerJobImpl : JobImpl(
    category = JobCategory.SELLER,
    name = "black_market_dealer",
    displayName = buildText { primary("Schwarzmarkthändler") },
    description = { },
    rules = { },
    income = 340,
    maxPlayers = 2,
), BlackMarketDealerJob