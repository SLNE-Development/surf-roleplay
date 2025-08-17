plugins {
    id("dev.slne.surf.surfapi.gradle.core")
}

dependencies {
    compileOnlyApi(libs.netty)
    compileOnlyApi(libs.datafixerupper)
    api(libs.bytebuddy)
    api(libs.kotlin.byte.buf.serializer)
}