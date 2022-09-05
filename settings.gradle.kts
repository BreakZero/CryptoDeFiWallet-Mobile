pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven(uri("https://maven.pkg.github.com/trustwallet/wallet-core")) {
            credentials {
                username = userProperty().getProperty("gpr.name")
                password = userProperty().getProperty("gpr.key")
            }
        }
    }
}

fun userProperty(): java.util.Properties {
    val properties = java.util.Properties()
    val localProperties = File(rootDir, "local.properties")

    if (localProperties.isFile) {
        java.io.InputStreamReader(
            java.io.FileInputStream(localProperties)
        ).use { reader ->
            properties.load(reader)
        }
    }
    return properties
}

rootProject.name = "DeFiWallet"
includeBuild("configuration-module")
include(":app")
include(":resource")
include(":onboarding:onboarding_domain")
include(":onboarding:onboarding_presentation")
include(":core")
include(":core-ui")
include(":wallet")
