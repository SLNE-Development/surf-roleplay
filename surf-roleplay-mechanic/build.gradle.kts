plugins {
    id("dev.slne.surf.surfapi.gradle.paper-raw")
}

dependencies {
    api(project(":surf-roleplay-api"))

    compileOnly(libs.surf.database)
}