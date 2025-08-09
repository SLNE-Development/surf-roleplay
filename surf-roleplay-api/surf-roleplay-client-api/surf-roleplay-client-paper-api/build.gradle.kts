plugins {
    id("dev.slne.surf.surfapi.gradle.paper-raw")
}

surfRawPaperApi {
    withCloudClientPaper()
}

dependencies {
    api(project(":surf-roleplay-api:surf-roleplay-client-api:surf-roleplay-client-common-api"))
}