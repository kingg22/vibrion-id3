import kotlinx.kover.gradle.plugin.dsl.CoverageUnit
import org.jetbrains.kotlin.gradle.dsl.JvmDefaultMode
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion
import org.jetbrains.kotlin.gradle.dsl.abi.ExperimentalAbiValidation

plugins {
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.dokka)
    alias(libs.plugins.kotlinxKover)
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.maven.publish)
}

group = "io.github.kingg22"
version = "0.7.0-SNAPSHOT"
description = "A lightweight Kotlin Multiplatform library to write ID3v2 tags in MP3 audio files."

kotlin {
    compilerOptions {
        languageVersion.set(KotlinVersion.KOTLIN_2_1)
        apiVersion.set(KotlinVersion.KOTLIN_2_1)
        extraWarnings.set(true)
        allWarningsAsErrors.set(true)
    }

    @OptIn(ExperimentalAbiValidation::class)
    abiValidation {
        enabled.set(true)
        filters {
            excluded {
                annotatedWith.add("$group.vibrion.id3.ExperimentalVibrionId3")
            }
        }
    }

    applyDefaultHierarchyTemplate()

    androidLibrary {
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

    // tier 2
    linuxX64()
    linuxArm64()
    // tier 3
    mingwX64()
    androidNativeArm32()
    androidNativeArm64()
    androidNativeX64()
    androidNativeX86()

    sourceSets.commonTest.dependencies {
        implementation(libs.kotlin.test)
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
