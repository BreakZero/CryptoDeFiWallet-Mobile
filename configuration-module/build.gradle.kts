plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    google()
}

dependencies {
    compileOnly("com.android.tools.build:gradle:7.0.4")
    compileOnly("com.android.tools.build:gradle-api:7.0.4")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.10")
}
tasks.withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompile::class.java) {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

gradlePlugin {
    plugins {
        create("Configuration") {
            id = "crypto.module.config"
            implementationClass = "com.crypto.configuration.CryptoModuleConfigPlugin"
        }
    }
}