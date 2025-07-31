import dev.slne.surf.surfapi.gradle.util.withSurfApiBukkit

plugins {
    id("dev.slne.surf.surfapi.gradle.paper-plugin")
}

dependencies {
    api(project(":surf-job-api"))
}

surfPaperPluginApi {
    mainClass("dev.slne.surf.cloud.bukkit.BukkitMain")
    generateLibraryLoader(false)
    authors.add("Ammo")

    runServer {
        withSurfApiBukkit()
    }
}