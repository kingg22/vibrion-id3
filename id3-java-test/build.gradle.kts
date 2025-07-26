plugins {
    java
}

group = "io.github.kingg22"
version = "unspecified"

dependencies {
    // hardcode because https://youtrack.jetbrains.com/issue/KMT-1428/Calling-kotlin-common-code-from-java-overload-and-non-primitive-types-cause-unresolved-overload-ambiguity
    @Suppress("kotlin:S6624")
    testImplementation("io.github.kingg22:vibrion-id3:0.5.0")
    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly(libs.junit.platform)
}

tasks.test {
    useJUnitPlatform()
}
