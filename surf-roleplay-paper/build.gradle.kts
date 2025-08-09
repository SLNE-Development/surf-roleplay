import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("dev.slne.surf.surfapi.gradle.paper-plugin")
}

surfPaperPluginApi {
    withCloudClientPaper()
    mainClass("dev.slne.surf.roleplay.paper.PaperMain")
}

dependencies {
    api(project(":surf-roleplay-core:surf-roleplay-core-client"))
    api(project(":surf-roleplay-api:surf-roleplay-client-api:surf-roleplay-client-paper-api"))
}

tasks.withType<ShadowJar> {
    destinationDirectory.set(rootProject.file("output"))
}