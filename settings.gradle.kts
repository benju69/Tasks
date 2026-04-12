pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "TaskManager"

include(":app")
include(":domain")
include(":data")
include(":feature:tasklist")
include(":feature:taskeditor")
include(":feature:settings")
include(":core")
include(":ui")
include(":desktopApp")
