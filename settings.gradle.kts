pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
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
