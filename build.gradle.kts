allprojects {
    group = "dev.slne.surf.roleplay"
    version = findProperty("version") as String
}

buildscript {
    repositories {
        gradlePluginPortal()
        maven("https://repo.slne.dev/repository/maven-public/") { name = "maven-public" }
    }
    dependencies {
        classpath("dev.slne.surf:surf-api-gradle-plugin:1.21.8+")
    }
}

subprojects {
    afterEvaluate {
        plugins.withType<JavaPlugin> {
            configure<JavaPluginExtension> {
                toolchain.languageVersion.set(JavaLanguageVersion.of(24))
            }
        }
    }
}