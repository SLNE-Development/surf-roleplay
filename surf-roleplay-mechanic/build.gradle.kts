plugins {
    id("dev.slne.surf.surfapi.gradle.paper-raw")
}

dependencies {
    compileOnly(project(":surf-roleplay-core"))
    compileOnlyApi(libs.surf.npc.api)
    compileOnly(project(":surf-job:surf-job-api"))
}