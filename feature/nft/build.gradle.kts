plugins {
  id("easy.android.feature")
  id("easy.android.library.compose")
  id("easy.android.library.jacoco")
}

android {
  namespace = "com.easy.defi.app.feature.nft"
}

dependencies {
  implementation(libs.accompanist.swiperefresh)
}

