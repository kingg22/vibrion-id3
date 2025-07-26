@file:OptIn(ExperimentalAbiValidation::class)

import kotlinx.kover.gradle.plugin.dsl.CoverageUnit
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.abi.ExperimentalAbiValidation

plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.dokka)
    alias(libs.plugins.kotlinxKover)
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.maven.publish)
}

group = "io.github.kingg22"
version = "0.6.0"

kotlin {
    compilerOptions {
        extraWarnings.set(true)
        allWarningsAsErrors.set(true)
    }
    abiValidation {
        enabled.set(true)
        filters {
            excluded {
                annotatedWith.add("$group.vibrion.id3.ExperimentalVibrionId3")
            }
        }
    }

    androidTarget {
        publishLibraryVariants("release")
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_1_8)
        }
    }

    jvm {
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }

    // tier 2
    linuxX64()
    linuxArm64()
    // tier 3
    mingwX64()
    androidNativeArm32()
    androidNativeArm64()
    androidNativeX64()
    androidNativeX86()

    sourceSets {
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "$group.vibrion.id3"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
    testOptions {
        unitTests.all {
            it.useJUnitPlatform()
        }
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "**/test/**"
            excludes += "**/commonTest/**"
        }
    }
    buildFeatures {
        buildConfig = true
    }
}

ktlint {
    version.set(libs.versions.ktlint.pinterest.get())
}

dokka.dokkaSourceSets.configureEach {
    skipEmptyPackages = true
    skipDeprecated = false
    reportUndocumented = true
    enableJdkDocumentationLink = true
    enableKotlinStdLibDocumentationLink = true
}

kover {
    reports.total {
        verify {
            rule("Basic Line Coverage") {
                minBound(60, CoverageUnit.LINE)
            }

            rule("Basic Branch Coverage") {
                minBound(20, CoverageUnit.BRANCH)
            }
        }
        filters.excludes {
            annotatedBy("$group.vibrion.id3.internal.KoverIgnore")
        }
    }
}

mavenPublishing {
    publishToMavenCentral()

    signAllPublications()

    coordinates(group.toString(), "vibrion-id3", version.toString())

    pom {
        name = "Vibrion ID3 – Kotlin Multiplatform ID3 Tag Writer"
        description = "A lightweight Kotlin Multiplatform library to write ID3v2 tags in MP3 audio files."
        inceptionYear = "2025"
        url = "https://github.com/kingg22/vibrion-id3"
        licenses {
            license {
                name = "The MIT License"
                url = "https://opensource.org/license/MIT"
                distribution = "repo"
            }
        }
        developers {
            developer {
                id = "kingg22"
                name = "Rey Acosta (Kingg22)"
                url = "https://github.com/kingg22"
            }
        }
        scm {
            url = "https://github.com/kingg22/vibrion-id3"
            connection = "scm:git:git://github.com/kingg22/vibrion-id3.git"
            developerConnection = "scm:git:ssh://git@github.com/kingg22/vibrion-id3.git"
        }
    }
}
