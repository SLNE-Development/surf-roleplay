plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

rootProject.name = "surf-roleplay"

include("surf-roleplay-api")
include("surf-roleplay-mechanic")
include("surf-roleplay-paper")

include("surf-job:surf-job-api")
include("surf-job:surf-job-paper")
