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
}

group = "io.github.kingg22"
version = "0.1.0"

kotlin {
    compilerOptions {
        extraWarnings.set(true)
        allWarningsAsErrors.set(true)
    }
    abiValidation {
        enabled.set(true)
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
