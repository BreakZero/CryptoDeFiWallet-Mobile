plugins {
  id("easy.android.library")
  id("easy.android.library.jacoco")
  kotlin("kapt")
}

dependencies {

  implementation(project(":core:data"))
  implementation(project(":core:model"))

  testImplementation(project(":core:testing"))

  implementation(libs.kotlinx.coroutines.android)
  implementation(libs.kotlinx.datetime)

  implementation(libs.hilt.android)
  kapt(libs.hilt.compiler)
}
