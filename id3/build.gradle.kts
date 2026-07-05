import kotlinx.kover.gradle.plugin.dsl.CoverageUnit
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmDefaultMode
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion
import org.jetbrains.kotlin.gradle.dsl.abi.ExperimentalAbiValidation
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.dokka)
    alias(libs.plugins.kotlinxKover)
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.maven.publish)
}

group = "io.github.kingg22"
version = "0.8.0"
description = "A lightweight Kotlin Multiplatform library to write ID3v2 tags in MP3 audio files."

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

kotlin {
    compilerOptions {
        languageVersion.set(KotlinVersion.KOTLIN_2_2)
        apiVersion.set(languageVersion)
        extraWarnings.set(true)
        allWarningsAsErrors.set(true)
    }

    @OptIn(ExperimentalAbiValidation::class)
    abiValidation {
        filters.exclude.annotatedWith.add("$group.vibrion.id3.ExperimentalVibrionId3")
    }

    applyDefaultHierarchyTemplate()

    android {
        namespace = "$group.vibrion.id3"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()

        packaging {
            resources {
                excludes.addAll(arrayOf("/META-INF/{AL2.0,LGPL2.1}", "**/test/**", "**/commonTest/**"))
            }
        }

        compilerOptions {
            jvmDefault.set(JvmDefaultMode.NO_COMPATIBILITY)
            jvmTarget.set(JvmTarget.JVM_1_8)
        }

        withHostTest {}
    }

    jvm {
        compilerOptions {
            jvmDefault.set(JvmDefaultMode.NO_COMPATIBILITY)
            jvmTarget.set(JvmTarget.JVM_1_8)
        }
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }

    js {
        browser()
        nodejs()
        binaries.library()
    }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
        nodejs()
        binaries.library()
    }

    // Tiers are in accordance with <https://kotlinlang.org/docs/native-target-support.html>
    // Tier 1
    // iOS
    listOf(
        iosArm64(),
        iosX64(),
        iosSimulatorArm64(),
        // macOs
        macosArm64(),
        // Tier 2
        // watchOS
        watchosArm32(),
        watchosArm64(),
        watchosSimulatorArm64(),
        // tvOS
        tvosArm64(),
        tvosSimulatorArm64(),
        watchosDeviceArm64(), // from tier 3
    ).forEach { appleTarget ->
        appleTarget.binaries.framework {
            baseName = "ktorgen-annotations"
            isStatic = true
        }
    }
    // linux
    linuxX64()
    linuxArm64()
    // Tier 3
    // android native
    androidNativeArm32()
    androidNativeArm64()
    androidNativeX86()
    androidNativeX64()
    // windows
    mingwX64()

    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    dependencies {
        testImplementation(libs.kotlin.test)
    }
}

ktlint {
    version.set(libs.versions.ktlint.pinterest)
}

dokka.dokkaSourceSets.configureEach {
    skipEmptyPackages = true
    skipDeprecated = false
    reportUndocumented = true
    enableJdkDocumentationLink = true
    enableKotlinStdLibDocumentationLink = true
}

kover.reports.total {
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

tasks.withType<JavaCompile>().configureEach {
    if (name.contains("TestJava")) {
        sourceCompatibility = "11"
        targetCompatibility = "11"
    }
}

tasks.withType<KotlinCompile>().configureEach {
    if (name.contains("TestKotlin")) {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
}

mavenPublishing {
    publishToMavenCentral()

    signAllPublications()

    coordinates(group.toString(), "vibrion-id3", version.toString())

    pom {
        name.set("Vibrion ID3 – Kotlin Multiplatform ID3 Tag Writer")
        description.set(project.description)
        inceptionYear.set("2025")
        url.set("https://github.com/kingg22/vibrion-id3")
        licenses {
            license {
                name.set("The MIT License")
                url.set("https://opensource.org/license/MIT")
                distribution.set("repo")
            }
        }
        developers {
            developer {
                id.set("kingg22")
                name.set("Rey Acosta (Kingg22)")
                url.set("https://github.com/kingg22")
            }
        }
        scm {
            url.set("https://github.com/kingg22/vibrion-id3")
            connection.set("scm:git:git://github.com/kingg22/vibrion-id3.git")
            developerConnection.set("scm:git:ssh://git@github.com/kingg22/vibrion-id3.git")
        }
    }
}
