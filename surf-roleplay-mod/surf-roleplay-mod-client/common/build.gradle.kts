plugins {
    id("dev.slne.surf.surfapi.gradle.core")
}

architectury {
    val enabledPlatforms = findProperty("enabled_platforms") as String
    common(enabledPlatforms.split(','))
}

val fabricLoaderVersion = libs.versions.fabric.loader.get()
val architecturyApiVersion = libs.versions.architectury.api.get()

dependencies {
    modImplementation("net.fabricmc:fabric-loader:$fabricLoaderVersion")
    modImplementation("dev.architectury:architectury:$architecturyApiVersion")
}
