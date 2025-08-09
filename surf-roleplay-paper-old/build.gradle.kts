import dev.slne.surf.surfapi.gradle.util.registerRequired
import dev.slne.surf.surfapi.gradle.util.withSurfApiBukkit

plugins {
    id("dev.slne.surf.surfapi.gradle.paper-plugin")
}

dependencies {
    api(project(":surf-roleplay-core"))
    api(project(":surf-roleplay-mechanic"))
    
    runtimeOnly(libs.surf.database)
}

surfPaperPluginApi {
    mainClass("dev.slne.surf.roleplay.paper.SurfRoleplay")
    generateLibraryLoader(false)
    authors.add("Ammo")
    foliaSupported(true)

    serverDependencies {
        registerRequired("surf-npc-bukkit")
    }

    runServer {
        withSurfApiBukkit()

        val npcVersion =
            libs.surf.npc.api.get().version ?: error("NPC API version is not specified")
        val npcAsset = "surf-npc-bukkit-${npcVersion}.jar"

        downloadPlugins {
            github(
                "slne-development",
                "surf-npc",
                "v$npcVersion",
                npcAsset
            )
        }
    }
}