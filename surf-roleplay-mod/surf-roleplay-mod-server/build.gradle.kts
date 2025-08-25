import dev.slne.surf.surfapi.gradle.util.registerRequired
import dev.slne.surf.surfapi.gradle.util.withSurfApiBukkit

plugins {
    id("dev.slne.surf.surfapi.gradle.paper-plugin")
}

surfPaperPluginApi {
    mainClass("dev.slne.surf.roleplay.mod.server.ModServer")
    foliaSupported(true)
    generateLibraryLoader(false)

    serverDependencies {
        registerRequired("ProtoWeaver")
    }

    runServer {
        withSurfApiBukkit()

        downloadPlugins {
            modrinth("protoweaver", "FUBheaaQ")
        }
    }
}

dependencies {
    api(project(":surf-roleplay-mod:surf-roleplay-mod-common"))
    compileOnlyApi(libs.protoweaver.paper)
}