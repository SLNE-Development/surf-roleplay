plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

buildscript {
    repositories {
        gradlePluginPortal()
        maven("https://repo.slne.dev/repository/maven-public/") { name = "maven-public" }
    }
    dependencies {
        classpath("dev.slne.surf:surf-api-gradle-plugin:1.21.7+")
    }
}

rootProject.name = "surf-roleplay"

include("surf-roleplay-api")
include("surf-roleplay-core")
include("surf-roleplay-mechanic")
include("surf-roleplay-paper")

include("surf-job:surf-job-api")
include("surf-job:surf-job-paper")
