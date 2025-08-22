package dev.slne.surf.roleplay.paper

import com.github.shynixn.mccoroutine.folia.SuspendingJavaPlugin
import dev.slne.surf.roleplay.core.common.RpInstance
import dev.slne.surf.roleplay.core.common.util.InternalContextHolder
import dev.slne.surf.roleplay.core.common.util.InternalRoleplayApi
import dev.slne.surf.roleplay.paper.command.activeIdentityCommand
import dev.slne.surf.roleplay.paper.command.balanceCommand
import org.bukkit.plugin.java.JavaPlugin
import org.springframework.beans.factory.getBean

@OptIn(InternalRoleplayApi::class)
class PaperMain : SuspendingJavaPlugin() {

    private val rpInstance get() = InternalContextHolder.instance.context.getBean<RpInstance>()

    override suspend fun onLoadAsync() {
        rpInstance.onLoad()

        balanceCommand()
        activeIdentityCommand()
    }

    override suspend fun onEnableAsync() {
        rpInstance.onEnable()
    }

    override suspend fun onDisableAsync() {
        rpInstance.onDisable()
    }

}

val plugin get() = JavaPlugin.getPlugin(PaperMain::class.java)