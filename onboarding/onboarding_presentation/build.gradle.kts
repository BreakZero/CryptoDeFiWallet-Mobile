import com.crypto.configuration.composeUI
import com.crypto.configuration.hiltDependencies

dependencies {
    implementation(project(":core"))
    implementation(project(":core-ui"))
    implementation(project(":resource"))
    implementation(project(":wallet"))

    composeUI()
    hiltDependencies()
}