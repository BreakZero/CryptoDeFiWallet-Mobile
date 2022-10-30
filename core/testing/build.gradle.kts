plugins {
  id("easy.android.library")
  id("easy.android.library.compose")
  id("easy.android.hilt")
}

android {
  namespace = "com.easy.defi.app.core.testing"
}

dependencies {
  implementation(project(":core:common"))
  api(libs.junit4)
  api(libs.androidx.test.core)
  api(libs.kotlinx.coroutines.test)
  api(libs.turbine)

  api(libs.androidx.test.espresso.core)
  api(libs.androidx.test.runner)
  api(libs.androidx.test.rules)
  api(libs.androidx.compose.ui.test)
  api(libs.hilt.android.testing)

  debugApi(libs.androidx.compose.ui.testManifest)
}
