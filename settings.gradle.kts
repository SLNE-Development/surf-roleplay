rootProject.name = "surf-roleplay"

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

include("surf-roleplay-api:surf-roleplay-common-api")
include("surf-roleplay-api:surf-roleplay-client-api:surf-roleplay-client-common-api")
include("surf-roleplay-api:surf-roleplay-client-api:surf-roleplay-client-paper-api")
include("surf-roleplay-api:surf-roleplay-client-api:surf-roleplay-client-velocity-api")
include("surf-roleplay-api:surf-roleplay-server-api")

include("surf-roleplay-core:surf-roleplay-core-common")
include("surf-roleplay-core:surf-roleplay-core-client")
include("surf-roleplay-paper")
include("surf-roleplay-velocity")
include("surf-roleplay-server")