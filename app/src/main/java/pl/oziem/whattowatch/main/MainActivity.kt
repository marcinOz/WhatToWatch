package pl.oziem.whattowatch.main

import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import pl.oziem.whattowatch.R

class MainActivity : DaggerAppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    setUpNavigation()
  }

  private fun setUpNavigation() {
    val navHostFragment =
      (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment)
    NavigationUI.setupWithNavController(bottomNavigation, navHostFragment.navController)
  }
}
