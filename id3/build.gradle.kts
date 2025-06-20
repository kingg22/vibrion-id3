import kotlinx.kover.gradle.plugin.dsl.CoverageUnit
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.dokka)
    alias(libs.plugins.kotlinxKover)
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.ktlint)
}

group = "io.github.kingg22"
version = "0.0.1"

kotlin {
    compilerOptions {
        extraWarnings.set(true)
        allWarningsAsErrors.set(true)
    }

    androidTarget {
        publishLibraryVariants("release")
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    jvm {
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }

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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
    testOptions {
        unitTests.all {
            it.useJUnitPlatform()
        }
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
            annotatedBy("$group.vibrion.id3.KoverIgnore")
        }
    }
}
