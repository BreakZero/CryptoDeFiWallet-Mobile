tasks.register("jacocoTestReport", JacocoReport::class) {
    dependsOn(listOf("testDebugUnitTest"))
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
    val kClasses = "${project.buildDir}/tmp/kotlin-classes/debug"
    val debugTree = fileTree(
        mapOf(
            "dir" to kClasses,
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
                    "outputs/unit_test_code_coverage/debugUnitTest/testDebugUnitTest.exec",
                    "outputs/code_coverage/debugAndroidTest/connected/*coverage.ec"
                )
            )
        )
    )
}
