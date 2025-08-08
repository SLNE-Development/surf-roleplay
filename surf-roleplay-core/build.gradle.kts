plugins {
    id("dev.slne.surf.surfapi.gradle.paper-raw")
}

dependencies {
    api(project(":surf-roleplay-api"))
    api(libs.surf.database)
    compileOnlyApi(libs.surf.npc.api)
}