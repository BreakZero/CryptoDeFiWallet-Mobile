package com.crypto.configuration

import com.crypto.configuration.dependencies.AndroidDeps
import com.crypto.configuration.dependencies.ComposeDeps
import com.crypto.configuration.dependencies.KotlinDeps
import com.crypto.configuration.dependencies.TestDeps
import org.gradle.kotlin.dsl.DependencyHandlerScope

fun DependencyHandlerScope.unitTestDependencies() {
    "testImplementation"("junit:junit:4.13.2")
    "testImplementation"(TestDeps.arch_core_testing)
    "testImplementation"(TestDeps.mockito_kotlin)
    "testImplementation"(TestDeps.coroutines_test)
    "testImplementation"(TestDeps.turbine)
    "testImplementation"(KotlinDeps.Ktor.mock)
}

fun DependencyHandlerScope.androidTestDependencies() {
    "androidTestImplementation"("androidx.test.ext:junit:1.1.3")
    "androidTestImplementation"("androidx.test.espresso:espresso-core:3.4.0")
    "androidTestImplementation"("com.google.guava:guava:30.1.1-android")
    "androidTestImplementation"(TestDeps.arch_core_testing)
    "androidTestImplementation"(TestDeps.mockito_kotlin)
    "androidTestImplementation"(TestDeps.coroutines_test)
    "androidTestImplementation"(TestDeps.turbine)
    "androidTestImplementation"(KotlinDeps.Ktor.mock)
}

fun DependencyHandlerScope.hiltDependencies() {
    "implementation"(AndroidDeps.Hilt.hilt_android)
    "kapt"(AndroidDeps.Hilt.hilt_android_compiler)
    "implementation"(AndroidDeps.Hilt.hilt_nav_compose)
}

fun DependencyHandlerScope.roomDependencies() {
    "kapt"(AndroidDeps.Room.compiler)
    "implementation"(AndroidDeps.Room.ktx)
    "implementation"(AndroidDeps.Room.runtime)
}

fun DependencyHandlerScope.composeUI() {
    "implementation"(ComposeDeps.ui)
    "implementation"(ComposeDeps.Material3.material3)
    "implementation"(ComposeDeps.preview)
    "implementation"(ComposeDeps.icons)
    "implementation"(ComposeDeps.Coil.compose)
    "implementation"(ComposeDeps.Coil.gif)
    "implementation"(ComposeDeps.paging)
    "implementation"(ComposeDeps.navigation)
    "implementation"(ComposeDeps.Accompanist.windowInsets)
    "implementation"(ComposeDeps.Accompanist.uiController)
    "implementation"(ComposeDeps.Accompanist.permission)
    "implementation"(ComposeDeps.Accompanist.navigation)
    "implementation"(ComposeDeps.Accompanist.swiperefresh)
    "implementation"(ComposeDeps.Accompanist.navigationAnimation)
    "implementation"(ComposeDeps.Accompanist.webView)
    "implementation"(ComposeDeps.Accompanist.pager)
    "implementation"(ComposeDeps.Accompanist.pager_indicators)
}