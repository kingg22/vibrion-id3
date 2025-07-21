plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    // alias(libs.plugins.aboutLibraries) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.dokka) apply false
    alias(libs.plugins.kotlinxKover) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.ktlint) apply false
    alias(libs.plugins.maven.publish) apply false
}
