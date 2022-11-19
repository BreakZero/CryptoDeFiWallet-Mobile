
plugins {
  id("easy.android.application")
  id("easy.android.application.compose")
  id("easy.android.application.jacoco")
  id("easy.android.hilt")
  id("jacoco")
}

android {
  defaultConfig {
    applicationId = "com.crypto.defi"
    versionCode = 1
    versionName = "0.0.1" // X.Y.Z; X = Major, Y = minor, Z = Patch level

    // Custom test runner to set up Hilt dependency graph
    testInstrumentationRunner = "com.easy.defi.app.core.testing.EasyTestRunner"
    vectorDrawables {
      useSupportLibrary = true
    }
  }
  val keyProperties = keyStoreProperties()
  signingConfigs {
    getByName("debug") {
      storeFile = rootProject.file(keyProperties.getProperty("storeFile"))
      storePassword = keyProperties.getProperty("storePassword")
      keyAlias = keyProperties.getProperty("keyAlias")
      keyPassword = keyProperties.getProperty("keyPassword")
    }
  }
  buildTypes {
    val debug by getting {
      applicationIdSuffix = ".debug"
      signingConfig = signingConfigs.getByName("debug")
    }
    val release by getting {
      isMinifyEnabled = true
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")

      // To publish on the Play store a private signing key is required, but to allow anyone
      // who clones the code to sign and run the release variant, use the debug signing key.
      // TODO: Abstract the signing configuration to a separate file to avoid hardcoding this.
      signingConfig = signingConfigs.getByName("debug")
    }
    val benchmark by creating {
      // Enable all the optimizations from release build through initWith(release).
      initWith(release)
      matchingFallbacks.add("release")
      // Debug key signing is available to everyone.
      signingConfig = signingConfigs.getByName("debug")
      // Only use benchmark proguard rules
      proguardFiles("benchmark-rules.pro")
      //  FIXME enabling minification breaks access to demo backend.
      isMinifyEnabled = false
      applicationIdSuffix = ".benchmark"
    }
  }

  packagingOptions {
    resources {
      excludes.add("/META-INF/{AL2.0,LGPL2.1}")
    }
  }
  testOptions {
    unitTests {
      isIncludeAndroidResources = true
    }
  }
  namespace = "com.easy.defi"
}

dependencies {

  implementation(project(":core:common"))
  implementation(project(":core:designsystem"))
  implementation(project(":core:ui"))
  implementation(project(":core:data"))
  implementation(project(":core:model"))

  implementation(project(":feature:onboarding"))
  implementation(project(":feature:assets"))
  implementation(project(":feature:nft"))
  implementation(project(":feature:dapp"))
  implementation(project(":feature:earn"))
  implementation(project(":feature:settings"))

  implementation(project(":sync:work"))

  implementation("com.trustwallet:wallet-core:3.0.6")

  androidTestImplementation(project(":core:testing"))
  androidTestImplementation(libs.androidx.navigation.testing)
  androidTestImplementation(kotlin("test"))
  debugImplementation(libs.androidx.compose.ui.testManifest)

  implementation(libs.accompanist.systemuicontroller)
  implementation(libs.androidx.activity.compose)
  implementation(libs.androidx.appcompat)
  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.core.splashscreen)
  implementation(libs.androidx.compose.runtime)
  implementation(libs.androidx.lifecycle.runtimeCompose)
  implementation(libs.androidx.compose.runtime.tracing)
  implementation(libs.androidx.compose.material3.windowSizeClass)
  implementation(libs.androidx.hilt.navigation.compose)
  implementation(libs.androidx.navigation.compose)
  implementation(libs.androidx.window.manager)
  implementation(libs.androidx.profileinstaller)

  implementation(libs.coil.kt)
  implementation(libs.coil.kt.svg)
  implementation(libs.coil.kt.gif)
}

// androidx.test is forcing JUnit, 4.12. This forces it to use 4.13
configurations.configureEach {
  resolutionStrategy {
    force(libs.junit4)
    // Temporary workaround for https://issuetracker.google.com/174733673
    force("org.objenesis:objenesis:2.6")
  }
}

tasks.register("installGitHook", Copy::class.java) {
  from(File(rootProject.rootDir, "scripts/pre-commit-macos"))
  into(File(rootProject.rootDir, ".git/hooks"))
  rename("pre-commit-macos", "pre-commit")
  fileMode = 509
}

tasks.getByPath(":app:preBuild").dependsOn("installGitHook")
