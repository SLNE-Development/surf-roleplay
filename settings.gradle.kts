rootProject.name = "surf-roleplay"

pluginManagement {
    repositories {
        mavenCentral()
        maven("https://maven.fabricmc.net")
        maven("https://maven.architectury.dev")
        maven("https://files.minecraftforge.net/maven")
        gradlePluginPortal()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

include("surf-roleplay-core:surf-roleplay-core-common")
include("surf-roleplay-core:surf-roleplay-core-client")
include("surf-roleplay-paper")
include("surf-roleplay-velocity")
include("surf-roleplay-server")

include("surf-roleplay-mod:surf-roleplay-mod-common")
include("surf-roleplay-mod:surf-roleplay-mod-server")

include("surf-roleplay-mod:surf-roleplay-mod-client:common")
include("surf-roleplay-mod:surf-roleplay-mod-client:fabric")
//include("surf-roleplay-mod:surf-roleplay-mod-client:neoforge")
