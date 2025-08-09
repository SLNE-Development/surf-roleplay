plugins {
    id("dev.slne.surf.surfapi.gradle.core")
}

surfCoreApi {
    withCloudServer()
}

dependencies {
    api(project(":surf-roleplay-api:surf-roleplay-common-api"))
}