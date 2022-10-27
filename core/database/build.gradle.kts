plugins {
  id("easy.android.library")
  id("easy.android.library.jacoco")
  id("easy.android.hilt")
  alias(libs.plugins.ksp)
}

android {
  defaultConfig {
    // The schemas directory contains a schema file for each version of the Room database.
    // This is required to enable Room auto migrations.
    // See https://developer.android.com/reference/kotlin/androidx/room/AutoMigration.
    ksp {
      arg("room.schemaLocation", "$projectDir/schemas")
    }

    testInstrumentationRunner = "com.easy.defi.app.core.testing.EasyTestRunner"
  }
  namespace = "com.easy.defi.app.core.database"
}

dependencies {
  implementation(project(":core:common"))
  implementation(project(":core:model"))

  implementation("net.zetetic:android-database-sqlcipher:4.5.0")

  implementation(libs.room.runtime)
  implementation(libs.room.ktx)
  ksp(libs.room.compiler)

  implementation(libs.kotlinx.coroutines.android)
  implementation(libs.kotlinx.datetime)

  androidTestImplementation(project(":core:testing"))
}
