plugins {
    id("dev.slne.surf.surfapi.gradle.paper-raw")
}

dependencies {
    api(project(":surf-roleplay-api"))
    api(libs.surf.database)
    api("dev.hsbrysk:caffeine-coroutines:2.0.2")
}