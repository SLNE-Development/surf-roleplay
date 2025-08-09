import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("dev.slne.surf.surfapi.gradle.core")
}

surfCoreApi {
    withCloudServer()
}

dependencies {
    api(project(":surf-roleplay-core:surf-roleplay-core-common"))
    api(project(":surf-roleplay-api:surf-roleplay-server-api"))
}

tasks.withType<ShadowJar> {
    destinationDirectory.set(rootProject.file("output"))
}