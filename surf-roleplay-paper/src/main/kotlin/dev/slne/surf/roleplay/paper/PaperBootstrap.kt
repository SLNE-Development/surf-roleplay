package dev.slne.surf.roleplay.paper

import dev.slne.surf.cloud.api.common.CloudInstance
import dev.slne.surf.cloud.api.common.startSpringApplication
import dev.slne.surf.roleplay.RoleplayApplication
import dev.slne.surf.roleplay.core.common.ContextHolderImpl
import io.papermc.paper.plugin.bootstrap.BootstrapContext
import io.papermc.paper.plugin.bootstrap.PluginBootstrap

@Suppress("UnstableApiUsage")
class PaperBootstrap : PluginBootstrap {
    override fun bootstrap(context: BootstrapContext) {
        ContextHolderImpl.instance.context =
            CloudInstance.startSpringApplication(RoleplayApplication::class)
    }
}