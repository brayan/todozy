pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "todozy"

include(":app")
include(":utility:kotlin-util")
include(":utility:android-util")
include(":ui-component:impl")
include(":ui-component:public")
include(":domain")
include(":feature:about:impl")
include(":feature:settings:public")
include(":feature:settings:public-android")
include(":feature:settings:impl")
include(":feature:alarm:public")
include(":feature:alarm:impl")
include(":feature:task-history:public")
include(":feature:task-history:impl")
include(":feature:task-form:public")
include(":feature:task-form:impl")
include(":feature:task-list:public")
include(":feature:task-list:impl")
include(":feature:task-details:public")
include(":feature:task-details:impl")
include(":feature:splash:impl")
include(":feature:navigation:public-android")
include(":platform:impl")
