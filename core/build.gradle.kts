plugins {
    alias(libs.plugins.kotlin.multiplatform)
}

kotlin {
    jvm()
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain.dependencies {
            implementation(project(":domain"))
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlin.stdlib)
        }
        commonTest.dependencies {
            api(libs.junit.jupiter.api)
            api(libs.kotlinx.coroutines.test)
            implementation(libs.junit.jupiter.params)
            implementation(libs.mockk)
            implementation(libs.kluent)
            implementation(libs.turbine)
        }
    }
}
