package dev.slne.surf.job.paper.job.jobs.gang

import dev.slne.surf.job.api.job.JobCategory
import dev.slne.surf.job.api.job.jobs.gang.ContractKillerJob
import dev.slne.surf.job.paper.job.JobImpl
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText

object ContractKillerJobImpl : JobImpl(
    category = JobCategory.GANG,
    name = "contract_killer",
    displayName = buildText { primary("Auftragsmörder") },
    description = { },
    rules = { },
    income = 50,
    maxPlayers = 2,
), ContractKillerJob