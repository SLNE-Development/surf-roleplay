plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

rootProject.name = "surf-job"

include("surf-job-api")
include("surf-job-paper")
