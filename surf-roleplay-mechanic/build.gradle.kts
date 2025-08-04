plugins {
    id("dev.slne.surf.surfapi.gradle.paper-raw")
}

dependencies {
    compileOnly(project(":surf-roleplay-core"))
    compileOnly(project(":surf-job:surf-job-api"))
    compileOnlyApi(libs.surf.npc.api)
}