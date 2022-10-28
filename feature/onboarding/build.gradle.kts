plugins {
  id("easy.android.feature")
  id("easy.android.library.compose")
  id("easy.android.library.jacoco")
}

android {
  namespace = "com.easy.defi.app.onboarding"
}

dependencies {
  implementation(libs.accompanist.pager)
  implementation(libs.accompanist.pager.indicators)
}

