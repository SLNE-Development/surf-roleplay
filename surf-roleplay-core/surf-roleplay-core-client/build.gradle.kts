plugins {
    id("dev.slne.surf.surfapi.gradle.core")
}

surfCoreApi {
    withCloudClientCommon()
}

dependencies {
    api(project(":surf-roleplay-api:surf-roleplay-client-api:surf-roleplay-client-common-api"))
    api(project(":surf-roleplay-core:surf-roleplay-core-common"))
}