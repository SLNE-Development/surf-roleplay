import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import net.fabricmc.loom.task.RemapJarTask

plugins {
    id("dev.slne.surf.surfapi.gradle.core")
}

architectury {
    platformSetupLoomIde()
    fabric()
}

configurations {
    create("common") {
        isCanBeResolved = true
        isCanBeConsumed = false
    }

    named("compileClasspath").get().extendsFrom(configurations.getByName("common"))
    named("runtimeClasspath").get().extendsFrom(configurations.getByName("common"))
    named("developmentFabric").get().extendsFrom(configurations.getByName("common"))

    create("shadowBundle") {
        isCanBeResolved = true
        isCanBeConsumed = false
    }
}


val fabricLoaderVersion = libs.versions.fabric.loader.get()
val fabricApiVersion = libs.versions.fabric.api.get()
val architecturyApiVersion = libs.versions.architectury.api.get()

dependencies {
    modImplementation("net.fabricmc:fabric-loader:$fabricLoaderVersion")
    modImplementation("net.fabricmc.fabric-api:fabric-api:$fabricApiVersion")
    modImplementation("dev.architectury:architectury-fabric:$architecturyApiVersion")

    add(
        "common", project(
            mapOf(
                "path" to ":surf-roleplay-mod:surf-roleplay-mod-client:common",
                "configuration" to "namedElements"
            )
        )
    )

    add(
        "shadowBundle", project(
            mapOf(
                "path" to ":surf-roleplay-mod:surf-roleplay-mod-client:common",
                "configuration" to "transformProductionFabric"
            )
        )
    )
}

tasks.processResources {
    inputs.property("version", project.version)

    filesMatching("fabric.mod.json") {
        expand("version" to project.version)
    }
}

tasks.withType<ShadowJar> {
    configurations = listOf(project.configurations.getByName("shadowBundle"))
    archiveClassifier.set("dev-shadow")
}

tasks.withType<RemapJarTask> {
    input.set(tasks.withType<ShadowJar>()["shadowJar"].archiveFile)
}
