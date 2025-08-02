plugins {
    id("dev.slne.surf.surfapi.gradle.paper-raw")
}

dependencies {
    api(project(":surf-job:surf-job-api"))
    
    compileOnly(libs.surf.database)
}