package br.com.sailboat.todozy.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ListAlt
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.stringResource
import androidx.core.view.isVisible
import androidx.fragment.app.commit
import androidx.navigation.fragment.NavHostFragment
import br.com.sailboat.todozy.databinding.ActivityHomeBinding
import br.com.sailboat.todozy.feature.navigation.android.HomeDestination
import br.com.sailboat.todozy.feature.navigation.android.HomeNavigationExtras.EXTRA_HOME_DESTINATION
import br.com.sailboat.todozy.feature.navigation.android.HomeTabNavigator
import br.com.sailboat.uicomponent.impl.theme.TodozyTheme
import br.com.sailboat.todozy.R as AppR
import br.com.sailboat.uicomponent.impl.R as UiR

class HomeActivity : AppCompatActivity(), HomeTabNavigator {
    private lateinit var binding: ActivityHomeBinding
    private var selectedTabId by mutableStateOf(AppR.id.nav_tasks)

    private val navHostIds =
        mapOf(
            AppR.id.nav_tasks to AppR.id.tasks_nav_host,
            AppR.id.nav_history to AppR.id.history_nav_host,
            AppR.id.nav_settings to AppR.id.settings_nav_host,
        )
    private val navGraphIds =
        mapOf(
            AppR.id.nav_tasks to AppR.navigation.nav_tasks,
            AppR.id.nav_history to AppR.navigation.nav_history,
            AppR.id.nav_settings to AppR.navigation.nav_settings,
        )
    private val bottomBarItems =
        listOf(
            BottomBarItem(
                id = AppR.id.nav_tasks,
                icon = Icons.AutoMirrored.Filled.ListAlt,
                title = UiR.string.label_tasks,
            ),
            BottomBarItem(
                id = AppR.id.nav_history,
                icon = Icons.Filled.History,
                title = UiR.string.history,
            ),
            BottomBarItem(
                id = AppR.id.nav_settings,
                icon = Icons.Filled.Settings,
                title = UiR.string.settings,
            ),
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ensureNavHostsCreated()
        configureBottomBar()
        applyStartDestination()
    }

    private fun configureBottomBar() {
        binding.homeBottomNav.setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        binding.homeBottomNav.setContent {
            TodozyTheme {
                BottomNavigation(
                    backgroundColor = MaterialTheme.colors.surface,
                    contentColor = MaterialTheme.colors.onSurface,
                ) {
                    bottomBarItems.forEach { item ->
                        val selected = selectedTabId == item.id
                        BottomNavigationItem(
                            selected = selected,
                            onClick = { onBottomTabSelected(item.id) },
                            icon = {
                                Icon(imageVector = item.icon, contentDescription = stringResource(id = item.title))
                            },
                            label = { Text(text = stringResource(id = item.title)) },
                            selectedContentColor = MaterialTheme.colors.primary,
                            unselectedContentColor = MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.medium),
                            alwaysShowLabel = true,
                        )
                    }
                }
            }
        }
    }

    private fun applyStartDestination() {
        val targetDestination =
            when (intent.getSerializableExtra(EXTRA_HOME_DESTINATION) as? HomeDestination) {
                HomeDestination.HISTORY -> AppR.id.nav_history
                HomeDestination.SETTINGS -> AppR.id.nav_settings
                else -> AppR.id.nav_tasks
            }
        selectTab(targetDestination, allowReselectPop = false)
    }

    private fun onBottomTabSelected(itemId: Int) {
        selectTab(itemId, allowReselectPop = true)
    }

    private fun selectTab(
        itemId: Int,
        allowReselectPop: Boolean,
    ) {
        if (itemId == AppR.id.nav_history) {
            notifyHistoryTabSelected()
        }

        if (allowReselectPop && itemId == selectedTabId) {
            popToRoot(itemId)
            return
        }

        selectedTabId = itemId
        showNavHost(itemId)
    }

    private fun showNavHost(itemId: Int) {
        val transaction = supportFragmentManager.beginTransaction()

        navHostIds.forEach { (destinationId, containerId) ->
            val fragment = supportFragmentManager.findFragmentById(containerId) ?: return@forEach
            val containerView = binding.root.findViewById<androidx.fragment.app.FragmentContainerView>(containerId)
            if (destinationId == itemId) {
                containerView?.isVisible = true
                transaction.show(fragment)
                transaction.setPrimaryNavigationFragment(fragment)
            } else {
                containerView?.isVisible = false
                transaction.hide(fragment)
            }
        }

        transaction.commit()
    }

    private fun popToRoot(itemId: Int) {
        val controller = navHostFragmentFor(itemId).navController
        controller.popBackStack(controller.graph.startDestinationId, inclusive = false)
    }

    private fun ensureNavHostsCreated() {
        navHostIds.keys.forEach { navHostFragmentFor(it) }
    }

    fun navHostFragmentFor(itemId: Int): NavHostFragment {
        val containerId = navHostIds.getValue(itemId)
        val existing = supportFragmentManager.findFragmentById(containerId) as? NavHostFragment
        if (existing != null) return existing

        val navHostFragment = NavHostFragment.create(navGraphIds.getValue(itemId))
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(containerId, navHostFragment)
        }
        supportFragmentManager.executePendingTransactions()
        return navHostFragment
    }

    override fun switchTo(destination: HomeDestination) {
        val targetId =
            when (destination) {
                HomeDestination.TASKS -> AppR.id.nav_tasks
                HomeDestination.HISTORY -> AppR.id.nav_history
                HomeDestination.SETTINGS -> AppR.id.nav_settings
            }
        selectTab(targetId, allowReselectPop = false)
    }

    companion object {
        fun createIntent(
            context: Context,
            destination: HomeDestination = HomeDestination.TASKS,
        ): Intent = Intent(context, HomeActivity::class.java).apply {
            putExtra(EXTRA_HOME_DESTINATION, destination)
        }
    }
}

private const val HISTORY_REFRESH_KEY = "history-refresh-request"

private data class BottomBarItem(
    val id: Int,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val title: Int,
)

private fun HomeActivity.notifyHistoryTabSelected() {
    val navController = this.navHostFragmentFor(AppR.id.nav_history).navController
    val historyBackStackEntry = navController.getBackStackEntry(AppR.id.nav_history)
    historyBackStackEntry.savedStateHandle[HISTORY_REFRESH_KEY] = System.currentTimeMillis()
}
