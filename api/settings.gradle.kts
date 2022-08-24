rootProject.name = "api"

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }

    plugins {
        id("org.jetbrains.dokka").version("1.7.10")
        id("com.android.application").version("7.2.1")
        id("org.jetbrains.kotlinx.kover") version "0.6.0-Beta"
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

include(":shared", ":scraper")
project(":shared").projectDir = file(File(rootDir, "android-app" + File.separator + "shared"))
project(":scraper").projectDir = file(File(rootDir, "android-app" + File.separator + "scraper"))