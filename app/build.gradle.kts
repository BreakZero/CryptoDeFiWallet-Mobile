import com.crypto.configuration.*
import com.crypto.configuration.dependencies.AndroidDeps
import com.crypto.configuration.dependencies.ComposeDeps

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
    kotlin("plugin.serialization") version "1.7.10" apply true
    jacoco apply true
}
//apply(from = "${rootProject.rootDir}/jacoco.gradle.kts")
android {
    compileSdk = AndroidBuildConfig.compileSdkVersion

    defaultConfig {
        applicationId = "com.crypto.defi"
        minSdk = AndroidBuildConfig.minSdkVersion
        targetSdk = AndroidBuildConfig.targetSdkVersion
        versionCode = AndroidBuildConfig.versionCode
        versionName = AndroidBuildConfig.versionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
//        vectorDrawables {
//            useSupportLibrary true
//        }
        kapt {
            arguments {
                arg("room.schemaLocation", "$projectDir/schemas")
            }
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
        create("release") {
            storeFile = rootProject.file(keyProperties.getProperty("storeFile"))
            storePassword = keyProperties.getProperty("storePassword")
            keyAlias = keyProperties.getProperty("keyAlias")
            keyPassword = keyProperties.getProperty("keyPassword")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
            isDebuggable = false
        }
        debug {
            signingConfig = signingConfigs.getByName("debug")
            isDebuggable = true
            isTestCoverageEnabled = true
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
            unitTests.isReturnDefaultValues = true
        }
    }

    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.3.0"
    }
    packagingOptions {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
            excludes.add("google/protobuf/*.proto")
        }
    }
}

dependencies {
    implementation(AndroidDeps.coreKtx)
    implementation(AndroidDeps.Lifecycle.runtime)
    implementation(AndroidDeps.Lifecycle.viewmodel_ktx)
    implementation(AndroidDeps.appcompat)
    implementation(AndroidDeps.activity_compose)

    implementation(ComposeDeps.constraintlayout)

    implementation(project(":resource"))
    implementation(project(":onboarding:onboarding_presentation"))
    implementation(project(":core"))
    implementation(project(":core-ui"))
    implementation(project(":wallet"))

    implementation("androidx.work:work-runtime-ktx:2.8.0-alpha04")

    composeUI()
    hiltDependencies()
    roomDependencies()

    unitTestDependencies()
    androidTestDependencies()
}

jacoco {
    toolVersion = "0.8.7"
    reportsDirectory.set(layout.buildDirectory.dir("jacoco"))
}

tasks.register("jacocoTestReport", JacocoReport::class) {
    dependsOn(listOf("testDebugUnitTest", "createDebugCoverageReport"))
    reports {
        xml.required.set(true)
        html.required.set(true)
        html.outputLocation.set(layout.buildDirectory.dir("jacocoHtml"))
    }
    val fileFilter = listOf(
        "**/R.class",
        "**/R$*.class",
        "**/BuildConfig.*",
        "**/Manifest*.*",
        "**/*Test*.*",
        "android/**/*.*",
        "**/*_Factory.*",
        "**/*_Provide*Factory*.*"
    )
    val jClasses = "${project.buildDir}/intermediates/javac/debug/classes"
    val kClasses = "${project.buildDir}/tmp/kotlin-classes/debug"

    val javaClasses = fileTree(
        mapOf(
            "dir" to jClasses,
            "excludes" to fileFilter
        )
    )

    val kotlinClasses = fileTree(
        mapOf(
            "dir" to kClasses,
            "excludes" to fileFilter
        )
    )

    val mainSrc = "${projectDir}/src/main/java"
    sourceDirectories.setFrom(files(mainSrc))
    classDirectories.setFrom(files(javaClasses, kotlinClasses))
    executionData.setFrom(
        fileTree(
            mapOf(
                "dir" to project.buildDir, "includes" to listOf(
                    "outputs/unit_test_code_coverage/debugUnitTest/testDebugUnitTest.exec",
                    "outputs/code_coverage/debugAndroidTest/connected/*/coverage.ec"
                )
            )
        )
    )
}

tasks.withType(Test::class.java) {
    configure<JacocoTaskExtension> {
        isEnabled = true
        excludes = listOf("jdk.internal.*")
        isIncludeNoLocationClasses = true
        setDestinationFile(layout.buildDirectory.file("jacoco/jacocoTest.exec").get().asFile)
        classDumpDir = layout.buildDirectory.dir("jacoco/classpathdumps").get().asFile
        output = JacocoTaskExtension.Output.FILE
    }
}