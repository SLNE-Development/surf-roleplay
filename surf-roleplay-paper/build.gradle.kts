import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import dev.slne.surf.surfapi.gradle.util.registerRequired

plugins {
    id("dev.slne.surf.surfapi.gradle.paper-plugin")
}

surfPaperPluginApi {
    withCloudClientPaper()
    mainClass("dev.slne.surf.roleplay.paper.PaperMain")
    foliaSupported(true)

    serverDependencies {
        registerRequired("surf-npc-bukkit")
    }
}

dependencies {
    api(project(":surf-roleplay-core:surf-roleplay-core-common"))

    compileOnly(libs.surf.npc.api)
}

tasks.withType<ShadowJar> {
    destinationDirectory.set(rootProject.file("output"))
}