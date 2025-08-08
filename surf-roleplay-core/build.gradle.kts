plugins {
    id("dev.slne.surf.surfapi.gradle.paper-raw")
}

dependencies {
    api(project(":surf-roleplay-api"))
    api(libs.surf.database)
    api(libs.caffeine.coroutines)
    compileOnlyApi(libs.surf.npc.api)
}