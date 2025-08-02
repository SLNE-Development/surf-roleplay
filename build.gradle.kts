import com.github.jengelman.gradle.plugins.shadow.ShadowPlugin
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import dev.slne.surf.surfapi.gradle.util.slnePublic

allprojects {
    repositories {
        slnePublic()
    }
}

subprojects {
    afterEvaluate {
        plugins.withType<JavaPlugin> {
            configure<JavaPluginExtension> {
                toolchain.languageVersion.set(JavaLanguageVersion.of(24))
            }
        }
        plugins.withType<ShadowPlugin> {
            tasks.withType<ShadowJar> {
                dependencies {
                    exclude(dependency("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm"))
                    exclude(dependency("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8"))
                    exclude(dependency("org.jetbrains.kotlin:kotlin-stdlib"))
                }
            }
        }
    }
}