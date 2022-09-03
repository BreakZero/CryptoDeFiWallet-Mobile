import com.crypto.configuration.composeUI
import com.crypto.configuration.dependencies.AndroidDeps

dependencies {
    implementation(AndroidDeps.zxing)

    // CameraX
    implementation(AndroidDeps.Camera.camera2)
    implementation(AndroidDeps.Camera.lifecycle)
    implementation(AndroidDeps.Camera.view)

    composeUI()
}