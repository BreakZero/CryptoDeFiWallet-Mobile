tasks.register("jacocoTestReport", JacocoReport::class) {
    dependsOn(listOf("testDebugUnitTest", "createDebugCoverageReport"))
    reports {
        xml.required.set(true)
        html.required.set(true)
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
    val debugTree = fileTree(
        mapOf(
            "dir" to "${buildDir}/intermediates/classes/debug",
            "excludes" to fileFilter
        )
    )
    val mainSrc = "${project.projectDir}/src/main/java"
    sourceDirectories.from(files(mainSrc))
    classDirectories.from(files(debugTree))
    executionData.from(
        fileTree(
            mapOf(
                "dir" to buildDir, "includes" to listOf(
                    "jacoco/testDebugUnitTest.exec",
                    "outputs/code-coverage/connected/*coverage.ec"
                )
            )
        )
    )
}

