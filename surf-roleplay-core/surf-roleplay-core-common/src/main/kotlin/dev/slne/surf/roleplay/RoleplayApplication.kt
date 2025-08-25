package dev.slne.surf.roleplay

import dev.slne.surf.cloud.api.common.SurfCloudApplication
import org.springframework.context.ApplicationContext

@SurfCloudApplication
class RoleplayApplication {
    companion object {
        lateinit var context: ApplicationContext
    }
}