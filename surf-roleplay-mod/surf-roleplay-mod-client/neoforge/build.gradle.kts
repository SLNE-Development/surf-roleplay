import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import net.fabricmc.loom.task.RemapJarTask

plugins {
    id("dev.slne.surf.surfapi.gradle.core")
}

architectury {
    platformSetupLoomIde()
    neoForge()
}

configurations {
    create("common") {
        isCanBeResolved = true
        isCanBeConsumed = false
    }

    named("compileClasspath").get().extendsFrom(configurations.getByName("common"))
    named("runtimeClasspath").get().extendsFrom(configurations.getByName("common"))
    named("developmentNeoForge").get().extendsFrom(configurations.getByName("common"))

    create("shadowBundle") {
        isCanBeResolved = true
        isCanBeConsumed = false
    }
}

val neoForgeVersion = libs.versions.neoforge.get()
val architecturyApiVersion = libs.versions.architectury.api.get()

dependencies {
    neoForge("net.neoforged:neoforge:$neoForgeVersion")
    modImplementation("dev.architectury:architectury-neoforge:$architecturyApiVersion")

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
                "configuration" to "transformProductionNeoForge"
            )
        )
    )
}

tasks.processResources {
    inputs.property("version", project.version)

    filesMatching("META-INF/neoforge.mods.toml") {
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
