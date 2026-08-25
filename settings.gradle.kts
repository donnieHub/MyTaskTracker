pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven { url = uri("https://repo.spring.io/snapshot") }
    }
}

rootProject.name = "myTaskTracker"
include("user-service")
