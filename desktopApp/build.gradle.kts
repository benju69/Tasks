import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.kotlin.compose)
}

kotlin {
    jvm()

    sourceSets {
        jvmMain.dependencies {
            implementation(project(":domain"))
            implementation(project(":data"))
            implementation(project(":feature:tasklist"))
            implementation(project(":feature:taskeditor"))
            implementation(project(":feature:settings"))
            implementation(project(":core"))
            implementation(project(":ui"))

            implementation(compose.desktop.currentOs)
            implementation(libs.koin.core)
            implementation(libs.sqldelight.sqlite.driver)
            implementation(libs.kotlinx.coroutines.core)
        }
    }
}

compose.desktop {
    application {
        mainClass = "fr.benju.tasks.desktop.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "TaskManager"
            packageVersion = "1.0.0"
        }
    }
}
