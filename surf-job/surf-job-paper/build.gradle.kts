plugins {
    id("dev.slne.surf.surfapi.gradle.paper-raw")
}

dependencies {
    api(project(":surf-job:surf-job-api"))
    api(project(":surf-roleplay-core"))

    compileOnly(libs.surf.database)
}