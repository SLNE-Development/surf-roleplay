import dev.slne.surf.surfapi.gradle.util.withSurfApiBukkit

plugins {
    id("dev.slne.surf.surfapi.gradle.paper-plugin")
}

dependencies {
    api(project(":surf-roleplay-core"))
    api(project(":surf-roleplay-mechanic"))

    api(project(":surf-job:surf-job-paper"))

    runtimeOnly(libs.surf.database)
}

surfPaperPluginApi {
    mainClass("dev.slne.surf.roleplay.paper.SurfRoleplay")
    generateLibraryLoader(false)
    authors.add("Ammo")

    runServer {
        withSurfApiBukkit()
    }
}