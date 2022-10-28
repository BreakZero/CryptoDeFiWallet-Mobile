plugins {
  id("easy.android.library")
  id("easy.android.hilt")
}

android {
  namespace = "com.easy.defi.app.sync.test"
}

dependencies {
  api(project(":sync:work"))
  implementation(project(":core:data"))
  implementation(project(":core:testing"))
}
