plugins {
  id("easy.android.library")
  id("easy.android.library.jacoco")
  id("easy.android.hilt")
}

android {
  namespace = "com.easy.defi.app.core.common"
}

dependencies {
  testImplementation(project(":core:testing"))

  api(libs.timber)

  implementation(libs.androidx.security.crypto)
  implementation(libs.androidx.dataStore.core)
  implementation(libs.androidx.dataStore.preferences)
  implementation(libs.kotlinx.coroutines.android)
  implementation(libs.kotlinx.datetime)
}

