buildscript {
    dependencies {
        classpath(libs.google.services)
        classpath("com.google.firebase:firebase-crashlytics-gradle:3.0.2")
        classpath("com.google.firebase:perf-plugin:1.4.2")
        classpath(libs.gradle)
        classpath(libs.androidx.navigation.safe.args.gradle.plugin)

    }
    repositories {
        google()
        mavenCentral()

    }
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.8.0" apply false
    id("com.android.library") version "8.8.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.24" apply false
}