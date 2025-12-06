package br.com.sailboat.todozy.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.navOptions
import br.com.sailboat.todozy.R
import br.com.sailboat.todozy.databinding.ActivityHomeBinding
import br.com.sailboat.todozy.feature.navigation.android.HomeDestination
import br.com.sailboat.todozy.feature.navigation.android.HomeNavigationExtras.EXTRA_HOME_DESTINATION
import br.com.sailboat.todozy.feature.navigation.android.HomeTabNavigator

class HomeActivity : AppCompatActivity(), HomeTabNavigator {
    private lateinit var binding: ActivityHomeBinding

    private val rootDestinations =
        setOf(
            R.id.nav_tasks,
            R.id.nav_history,
            R.id.nav_settings,
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = resolveNavController()
        configureBottomNav(navController)
        applyStartDestination(navController)
    }

    private fun configureBottomNav(navController: NavController) {
        binding.homeBottomNav.setOnItemSelectedListener { item ->
            navigateToRoot(item.itemId, navController)
            true
        }

        binding.homeBottomNav.setOnItemReselectedListener { item ->
            if (navController.currentDestination?.id == item.itemId) {
                navController.popBackStack(item.itemId, inclusive = false)
            }
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.homeBottomNav.isVisible = destination.id in rootDestinations
        }
    }

    private fun applyStartDestination(navController: NavController) {
        val targetDestination =
            when (intent.getSerializableExtra(EXTRA_HOME_DESTINATION) as? HomeDestination) {
                HomeDestination.HISTORY -> R.id.nav_history
                HomeDestination.SETTINGS -> R.id.nav_settings
                else -> R.id.nav_tasks
            }
        if (binding.homeBottomNav.selectedItemId != targetDestination) {
            binding.homeBottomNav.selectedItemId = targetDestination
            navigateToRoot(targetDestination, navController)
        }
    }

    private fun navigateToRoot(
        itemId: Int,
        navController: NavController,
    ) {
        val options =
            navOptions {
                launchSingleTop = true
                restoreState = true
                popUpTo(navController.graph.startDestinationId) {
                    saveState = true
                }
            }
        navController.navigate(itemId, null, options)
    }

    private fun resolveNavController(): NavController {
        val navHost =
            supportFragmentManager.findFragmentById(R.id.home_nav_host) as NavHostFragment
        return navHost.navController
    }

    override fun switchTo(destination: HomeDestination) {
        val navController = resolveNavController()
        val targetId =
            when (destination) {
                HomeDestination.TASKS -> R.id.nav_tasks
                HomeDestination.HISTORY -> R.id.nav_history
                HomeDestination.SETTINGS -> R.id.nav_settings
            }
        if (binding.homeBottomNav.selectedItemId != targetId) {
            binding.homeBottomNav.selectedItemId = targetId
        }
        navigateToRoot(targetId, navController)
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
