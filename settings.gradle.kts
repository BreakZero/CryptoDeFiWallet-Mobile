pluginManagement {
    includeBuild("build-logic")
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
    val localProperties = File("./keystore", "github_token.properties")

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
include(":app")
include(":core:testing")
include(":core:common")
include(":core:ui")
include(":core:data")
include(":core:database")
include(":core:domain")
include(":core:datastore")
include(":core:model")

include(":feature:onboarding")
include(":feature:settings")
include(":feature:multiwallet")
include(":core:network")
include(":feature:assets")
include(":core:designsystem")
include(":feature:dapp")
include(":feature:nft")
include(":feature:earn")
include(":sync:sync-test")
include(":sync:work")
