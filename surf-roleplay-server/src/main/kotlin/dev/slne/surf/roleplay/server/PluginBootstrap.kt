package dev.slne.surf.roleplay.server

import dev.slne.surf.cloud.api.common.CloudInstance
import dev.slne.surf.cloud.api.common.startSpringApplication
import dev.slne.surf.cloud.api.server.plugin.bootstrap.BootstrapContext
import dev.slne.surf.cloud.api.server.plugin.bootstrap.StandalonePluginBootstrap
import dev.slne.surf.roleplay.RoleplayApplication
import dev.slne.surf.roleplay.core.common.ContextHolderImpl

class PluginBootstrap : StandalonePluginBootstrap {
    override suspend fun bootstrap(context: BootstrapContext) {
        ContextHolderImpl.instance.context =
            CloudInstance.startSpringApplication(RoleplayApplication::class)
    }
}