package dev.slne.surf.job.paper.job.jobs.seller

import dev.slne.surf.job.api.job.jobs.seller.BlackMarketDealerJob
import dev.slne.surf.job.paper.job.JobImpl
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText

object BlackMarketDealerJobImpl : JobImpl(
    name = "black_market_dealer",
    displayName = buildText { primary("Schwarzmarkthändler") },
    income = 340,
    maxPlayers = 1,
), BlackMarketDealerJob