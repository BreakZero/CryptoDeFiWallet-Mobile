buildscript {
  repositories {
    google()
    mavenCentral()
  }
}

plugins {
  alias(libs.plugins.android.application) apply false
  alias(libs.plugins.kotlin.jvm) apply false
  alias(libs.plugins.kotlin.serialization) apply false
  alias(libs.plugins.hilt) apply false
  alias(libs.plugins.secrets) apply false
  id("org.jetbrains.kotlin.android") version "1.7.10" apply false
}

subprojects {
  this.apply(from = "${rootProject.rootDir}/lint.gradle.kts")
}

tasks.register("clean", Delete::class.java) {
  delete(rootProject.buildDir)
}
