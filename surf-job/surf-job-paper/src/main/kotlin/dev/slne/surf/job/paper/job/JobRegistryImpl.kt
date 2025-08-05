package dev.slne.surf.job.paper.job

import com.google.auto.service.AutoService
import dev.slne.surf.job.api.job.Job
import dev.slne.surf.job.api.job.JobRegistry
import dev.slne.surf.job.paper.job.jobs.gang.*
import dev.slne.surf.job.paper.job.jobs.gang.GangsterJobImpl.GangsterChiefJobImpl
import dev.slne.surf.job.paper.job.jobs.gang.MafiaJobImpl.MafiaChiefJobImpl
import dev.slne.surf.job.paper.job.jobs.gang.RebelJobImpl.RebelChiefJobImpl
import dev.slne.surf.job.paper.job.jobs.gang.ThiefJobImpl.ThiefChiefJobImpl
import dev.slne.surf.job.paper.job.jobs.neutral.*
import dev.slne.surf.job.paper.job.jobs.seller.*
import dev.slne.surf.job.paper.job.jobs.state.*
import dev.slne.surf.job.paper.job.jobs.state.FirefighterJobImpl.FirefighterChiefJobImpl
import dev.slne.surf.job.paper.job.jobs.state.PoliceJobImpl.SergeantJobImpl
import dev.slne.surf.surfapi.core.api.util.freeze
import dev.slne.surf.surfapi.core.api.util.mutableObjectSetOf
import net.kyori.adventure.util.Services

@AutoService(JobRegistry::class)
class JobRegistryImpl : JobRegistry, Services.Fallback {

    val VERSION = "v1.0"

    private val _jobs = mutableObjectSetOf<Job>()
    override val jobs get() = _jobs.freeze()

    fun registerJobs() {
        // neutral jobs
        registerJob(BankerJobImpl)
        registerJob(CitizenJobImpl)
        registerJob(DjJobImpl)
        registerJob(FarmerJobImpl)
        registerJob(HomelessJobImpl)
        registerJob(LawyerJobImpl)
        registerJob(MinerJobImpl)
        registerJob(OilRefinerJobImpl)
        registerJob(PriestJobImpl)
        registerJob(SecurityJobImpl)
        registerJob(TaxiDriverJobImpl)

        // seller jobs
        registerJob(BlackMarketDealerJobImpl)
        registerJob(ButcherJobImpl)
        registerJob(CookJobImpl)
        registerJob(MechanicJobImpl)
        registerJob(WeaponDealerJobImpl)

        // state jobs
        registerJob(FirefighterJobImpl)
        registerJob(FirefighterChiefJobImpl)
        registerJob(PoliceJobImpl)
        registerJob(SergeantJobImpl)
        registerJob(RescueServiceJobImpl)
        registerJob(DispatchJobImpl)

        registerJob(CustomOfficerJobImpl)
        registerJob(DoctorJobImpl)
        registerJob(MayorJobImpl)
        registerJob(PrisonGuardJobImpl)
        registerJob(SecretServiceJobImpl)
        registerJob(TaskForceJobImpl)

        // gang jobs
        registerJob(ContractKillerJobImpl)
        registerJob(GangsterJobImpl)
        registerJob(GangsterChiefJobImpl)
        registerJob(MafiaJobImpl)
        registerJob(MafiaChiefJobImpl)
        registerJob(RebelJobImpl)
        registerJob(RebelChiefJobImpl)
        registerJob(TerroristJobImpl)
        registerJob(ThiefJobImpl)
        registerJob(ThiefChiefJobImpl)
    }

    private fun registerJob(job: Job) = _jobs.add(job)

    @Suppress("UNCHECKED_CAST")
    override fun <T : Job> getJob(jobClass: Class<T>) =
        jobs.firstOrNull { jobClass.isAssignableFrom(it.javaClass) } as? T
            ?: throw IllegalArgumentException("Job of class ${jobClass.name} is not registered in the JobRegistry.")

    override fun checkJobKeepRequirements() = _jobs.all { it.performKeepRequirementsCheck() }
}

val jobRegistryImpl get() = JobRegistry.INSTANCE as JobRegistryImpl