@file:OptIn(ExperimentalWasmDsl::class, ExperimentalKotlinGradlePluginApi::class)

import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
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
    wasmJs {
        browser()
        nodejs()
        useEsModules()
        binaries.library()
        generateTypeScriptDefinitions()
    }
    wasmWasi {
        nodejs()
        binaries.library()
    }

    applyDefaultHierarchyTemplate {
        common {
            group("androidTargetAndJvm") {
                withAndroidTarget()
                withJvm()
            }
        }
    }

    sourceSets {
        commonMain.dependencies {
        }
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
    suppressedFiles.from(layout.buildDirectory.dir("generated"))
    // not used, but ok
    externalDocumentationLinks {
        register("kotlinx.coroutines") {
            url("https://kotlinlang.org/api/kotlinx.coroutines/")
        }
        register("kotlinx.serialization") {
            url("https://kotlinlang.org/api/kotlinx.serialization/")
        }
        register("kotlinx-datetime") {
            url("https://kotlinlang.org/api/kotlinx-datetime/")
            packageListUrl("https://kotlinlang.org/api/kotlinx-datetime/kotlinx-datetime/package-list")
        }
        register("ktor-client") {
            url("https://api.ktor.io/ktor-client/")
            packageListUrl("https://api.ktor.io/package-list")
        }
        register("kermit-logger") {
            url("https://kermit.touchlab.co/htmlMultiModule/")
        }
    }
}
