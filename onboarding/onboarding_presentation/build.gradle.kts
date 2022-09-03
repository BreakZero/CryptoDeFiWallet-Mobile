import com.crypto.configuration.composeUI

dependencies {
    implementation(project(":core"))
    implementation(project(":core-ui"))
    implementation(project(":resource"))

    composeUI()
}