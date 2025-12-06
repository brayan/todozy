package br.com.sailboat.todozy.feature.navigation.android

enum class HomeDestination {
    TASKS,
    HISTORY,
    SETTINGS,
}

interface HomeTabNavigator {
    fun switchTo(destination: HomeDestination)
}

object HomeNavigationExtras {
    const val EXTRA_HOME_DESTINATION = "extra_home_destination"
}
