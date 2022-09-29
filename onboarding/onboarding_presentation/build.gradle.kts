import com.crypto.configuration.androidTestDependencies
import com.crypto.configuration.composeUI
import com.crypto.configuration.hiltDependencies
import com.crypto.configuration.unitTestDependencies

plugins {
    jacoco apply true
}
apply(from = "${rootProject.rootDir}/jacoco.gradle.kts")
dependencies {
    implementation(project(":core"))
    implementation(project(":core-ui"))
    implementation(project(":resource"))
    implementation(project(":wallet"))

    composeUI()
    hiltDependencies()
    unitTestDependencies()
    androidTestDependencies()
}