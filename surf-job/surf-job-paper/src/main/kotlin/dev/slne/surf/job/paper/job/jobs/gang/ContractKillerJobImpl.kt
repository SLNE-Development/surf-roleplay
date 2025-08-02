package dev.slne.surf.job.paper.job.jobs.gang

import dev.slne.surf.job.api.job.jobs.gang.ContractKillerJob
import dev.slne.surf.job.paper.job.JobImpl
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText

object ContractKillerJobImpl : JobImpl(
    name = "contract_killer",
    displayName = buildText { primary("Auftragsmörder") },
    income = 50,
    maxPlayers = 1,
), ContractKillerJob