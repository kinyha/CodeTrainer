plugins {
    `java-library`
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    implementation(project(":fixtures"))
    implementation(platform(libs.spring.boot.bom))
    implementation(libs.spring.web)
    implementation(libs.spring.validation)
    implementation(libs.spring.data.jpa)
    implementation(libs.spring.kafka)

    testImplementation(libs.spring.test)
    testImplementation(libs.spring.kafka.test)
    testImplementation(platform(libs.testcontainers.bom))
    testImplementation(libs.testcontainers.junit)
    testImplementation(libs.testcontainers.postgresql)
    testImplementation(libs.testcontainers.kafka)
    testRuntimeOnly(libs.postgresql)
}

kotlin {
    jvmToolchain(21)
}
