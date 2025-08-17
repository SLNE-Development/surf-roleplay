plugins {
    id("dev.slne.surf.surfapi.gradle.core")
    alias(libs.plugins.loom)
}

base {
    archivesName.set(project.extra["archives_base_name"] as String)
}

fabricApi {
    configureDataGeneration {
        client = true
    }
}

dependencies {
    val minecraftVersion = libs.versions.minecraft.get()
    val yarnMappings = libs.versions.yarn.mappings.get()
    val loaderVersion = libs.versions.fabric.loader.get()
    val fabricVersion = libs.versions.fabric.api.get()
    val fabricKotlinVersion = libs.versions.fabric.kotlin.get()

    minecraft("com.mojang:minecraft:$minecraftVersion")
    mappings("net.fabricmc:yarn:$yarnMappings:v2")
    modImplementation("net.fabricmc:fabric-loader:$loaderVersion")

    implementation(project(":surf-roleplay-mod:surf-roleplay-mod-common"))
    implementation(libs.protoweaver.fabric)

    modImplementation("net.fabricmc.fabric-api:fabric-api:$fabricVersion")
    modImplementation("net.fabricmc:fabric-language-kotlin:$fabricKotlinVersion")
}

tasks.processResources {
    from(project.project(":surf-roleplay-mod:surf-roleplay-mod-common").sourceSets.main.get().resources)

    inputs.property("version", project.version)

    filesMatching("fabric.mod.json") {
        expand("version" to inputs.properties["version"])
    }
}

tasks.jar {
    inputs.property("archivesName", base.archivesName.get())

    from("LICENSE") {
        rename { "${it}_${inputs.properties["archivesName"]}" }
    }
}
