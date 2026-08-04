plugins {
    `java-library`
}

dependencies {
    implementation(project(":fixtures"))
    implementation(platform(libs.spring.boot.bom))
    implementation(libs.spring.web)
    implementation(libs.spring.data.jpa)

    testImplementation(libs.spring.test)
}
