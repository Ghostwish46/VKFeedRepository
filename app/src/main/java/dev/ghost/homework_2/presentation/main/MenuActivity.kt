package dev.ghost.homework_2.presentation.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import dev.ghost.homework_2.R
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_menu.*

class MenuActivity : AppCompatActivity(R.layout.activity_menu) {
    private lateinit var menuViewModel: MenuViewModel
    private val compositeDisposable = CompositeDisposable()

    lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        menuViewModel = ViewModelProvider(this).get(MenuViewModel::class.java)

        navController = Navigation.findNavController(this, R.id.navHostFragment)

        val appBarConfigurationIds =
            setOf(R.id.navigation_feed, R.id.navigation_favourite, R.id.navigation_profile)

        navView.setOnNavigationItemSelectedListener { menuItem ->
            if (menuItem.itemId == navController.currentBackStackEntry?.destination?.id)
                return@setOnNavigationItemSelectedListener false
            appBarConfigurationIds.forEach { fragmentId ->
                if (fragmentId == menuItem.itemId) {
                    navController.navigate(
                        fragmentId, null, NavOptions.Builder()
                            .setLaunchSingleTop(true)
                            .setPopUpTo(navController.currentDestination!!.id, true)
                            .build()
                    )
                    return@setOnNavigationItemSelectedListener true
                }
            }
            false
        }

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            if (appBarConfigurationIds.indexOf(destination.id) != -1) {
                controller.graph.startDestination = destination.id
            }
        }

        val disposablePosts =
            menuViewModel.getFavouritePosts().observeOn(AndroidSchedulers.mainThread())
                .subscribe { posts ->
                    navView.menu.findItem(R.id.navigation_favourite).isVisible = posts.isNotEmpty()
                    if (posts.isEmpty() && navController.currentDestination?.id == R.id.navigation_favourite)
                        navController.navigateUp()
                }
        compositeDisposable.add(disposablePosts)
    }

    override fun onSupportNavigateUp(): Boolean =
        navController.navigateUp()

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }
}