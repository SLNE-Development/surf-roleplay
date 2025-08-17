plugins {
    id("dev.slne.surf.surfapi.gradle.paper-plugin")
}

surfPaperPluginApi {
    mainClass("dev.slne.surf.roleplay.mod.server.ModServer")
    foliaSupported(true)
}

dependencies {
    api(project(":surf-roleplay-mod:surf-roleplay-mod-common"))
}