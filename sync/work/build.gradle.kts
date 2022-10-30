plugins {
  id("easy.android.library")
  id("easy.android.library.jacoco")
  id("easy.android.hilt")
}

android {
  defaultConfig {
    testInstrumentationRunner = "com.easy.defi.app.core.testing.EasyTestRunner"
  }
  namespace = "com.easy.defi.app.sync.work"
}

dependencies {
  implementation(project(":core:common"))
  implementation(project(":core:model"))
  implementation(project(":core:data"))

  implementation(libs.kotlinx.coroutines.android)

  implementation(libs.androidx.lifecycle.livedata.ktx)
  implementation(libs.androidx.tracing.ktx)
  implementation(libs.androidx.startup)
  implementation(libs.androidx.work.ktx)
  implementation(libs.hilt.ext.work)

  testImplementation(project(":core:testing"))
  androidTestImplementation(project(":core:testing"))

  kapt(libs.hilt.ext.compiler)

  androidTestImplementation(libs.androidx.work.testing)
}
