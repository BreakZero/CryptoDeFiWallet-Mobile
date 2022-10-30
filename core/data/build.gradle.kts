plugins {
  id("easy.android.library")
  id("easy.android.library.jacoco")
  id("easy.android.hilt")
  id("kotlinx-serialization")
}

android {
  namespace = "com.easy.defi.app.core.data"
}

dependencies {
  implementation(project(":core:common"))
  implementation(project(":core:model"))
  implementation(project(":core:database"))
  implementation(project(":core:network"))
  implementation(project(":core:datastore"))

  testImplementation(project(":core:testing"))

  implementation("com.trustwallet:wallet-core:3.0.6")

  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.compose.paging)

  implementation(libs.kotlinx.datetime)
  implementation(libs.kotlinx.coroutines.android)
  implementation(libs.kotlinx.serialization.json)
}

