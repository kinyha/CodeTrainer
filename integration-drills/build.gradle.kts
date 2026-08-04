plugins {
    `java-library`
}

dependencies {
    implementation(project(":fixtures"))
    implementation(platform(libs.spring.boot.bom))
    implementation(libs.spring.kafka)

    testImplementation(platform(libs.testcontainers.bom))
    testImplementation(libs.testcontainers.junit)
    testImplementation(libs.testcontainers.postgresql)
    testImplementation(libs.testcontainers.kafka)
    testImplementation(libs.spring.kafka.test)
    testRuntimeOnly(libs.postgresql)
}

val testSourceSet = sourceSets.test.get()

tasks.register<Test>("integrationTest") {
    description = "Runs tests tagged with @Tag(\"integration\")."
    group = LifecycleBasePlugin.VERIFICATION_GROUP
    testClassesDirs = testSourceSet.output.classesDirs
    classpath = testSourceSet.runtimeClasspath
    shouldRunAfter(tasks.test)
    useJUnitPlatform {
        includeTags("integration")
        excludeTags("slow")
    }
}
