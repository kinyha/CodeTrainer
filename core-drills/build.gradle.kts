plugins {
    `java-library`
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    implementation(project(":fixtures"))
}

kotlin {
    jvmToolchain(21)
}
