package dev.slne.surf.roleplay.paper

import com.github.shynixn.mccoroutine.folia.SuspendingJavaPlugin
import dev.slne.surf.roleplay.RoleplayApplication
import dev.slne.surf.roleplay.core.common.RpInstance
import dev.slne.surf.roleplay.paper.command.activeIdentityCommand
import dev.slne.surf.roleplay.paper.command.balanceCommand
import org.bukkit.plugin.java.JavaPlugin
import org.springframework.beans.factory.getBean

class PaperMain : SuspendingJavaPlugin() {
    private val rpInstance get() = RoleplayApplication.context.getBean<RpInstance>()

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