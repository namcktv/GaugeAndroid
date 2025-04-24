// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false

}
buildscript {
    repositories {
        google()
        mavenCentral()
        mavenLocal()
        maven { url = uri("https://jitpack.io") }
    }
    dependencies {
        classpath ("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.0")
        classpath ("com.android.tools.build:gradle:7.4.2") // Phiên bản mới nhất

    }
}
//allprojects {
//    repositories {
//        maven { url = uri("https://jitpack.io") }
//    }
//}
