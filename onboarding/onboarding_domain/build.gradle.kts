import com.crypto.configuration.androidTestDependencies
import com.crypto.configuration.unitTestDependencies

dependencies {
    implementation(project(":core"))

    unitTestDependencies()
    androidTestDependencies()
}