import com.google.protobuf.gradle.builtins
import com.google.protobuf.gradle.generateProtoTasks
import com.google.protobuf.gradle.protobuf
import com.google.protobuf.gradle.protoc

plugins {
  id("easy.android.library")
  id("easy.android.library.jacoco")
  id("easy.android.hilt")
  alias(libs.plugins.protobuf)
}

android {
  defaultConfig {
    consumerProguardFiles("consumer-proguard-rules.pro")
  }
  namespace = "com.easy.defi.app.core.datastore"
}

// Setup protobuf configuration, generating lite Java and Kotlin classes
protobuf {
  protoc {
    artifact = libs.protobuf.protoc.get().toString()
  }
  generateProtoTasks {
    all().forEach { task ->
      task.builtins {
        val java by registering {
          option("lite")
        }
        val kotlin by registering {
          option("lite")
        }
      }
    }
  }
}

dependencies {
  implementation(project(":core:common"))
  implementation(project(":core:model"))

  testImplementation(project(":core:testing"))

  implementation(libs.kotlinx.coroutines.android)

  implementation(libs.androidx.dataStore.core)
  implementation(libs.protobuf.kotlin.lite)
}
