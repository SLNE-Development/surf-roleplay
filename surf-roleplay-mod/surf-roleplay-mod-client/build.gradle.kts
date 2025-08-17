import net.fabricmc.loom.api.LoomGradleExtensionAPI

plugins {
    id("dev.slne.surf.surfapi.gradle.core")
    alias(libs.plugins.architectury.base)
    alias(libs.plugins.architectury.loom) apply false
}

val minecraftVersion = libs.versions.minecraft.get()
val modName = rootProject.findProperty("archives_name") as String

architectury {
    minecraft = minecraftVersion
}

repositories {
    maven("https://maven.neoforged.net/releases")
}

subprojects {
    apply(plugin = "dev.architectury.loom")
    apply(plugin = "architectury-plugin")

    base {
        archivesName = "$modName-${project.name}"
    }

    val loomExtension = extensions.getByType<LoomGradleExtensionAPI>()

    dependencies {
        "minecraft"("net.minecraft:minecraft:$minecraftVersion")
        "mappings"(loomExtension.officialMojangMappings())
    }
}
