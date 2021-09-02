package me.shahim.booksy

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import me.shahim.booksy.databinding.ActivityMainBinding
import me.shahim.booksy.ui.account.AccountViewModel
import me.shahim.booksy.ui.login.LoginActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var mainViewModel: AccountViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainViewModel = ViewModelProvider(this).get(AccountViewModel::class.java)

        init()
    }

    private fun init() {
        mainViewModel.loggedIn.observe(this) { loggedIn ->
            if (!loggedIn) {
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                finish()
            } else {

                val navView: BottomNavigationView = binding.navView

                val navController = findNavController(R.id.nav_host_fragment_activity_main)
                // Passing each menu ID as a set of Ids because each
                // menu should be considered as top level destinations.
                val appBarConfiguration = AppBarConfiguration(setOf(
                    R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications))
                setupActionBarWithNavController(navController, appBarConfiguration)
                navView.setupWithNavController(navController)
                navController.addOnDestinationChangedListener { controller, destination, arguments ->
                    when (destination.id) {
//                        R.id.trainingListFragment,R.id.beaconListFragment, R.id.bayListFragment, R.id.settingsFragment -> {
//                            nav_view.visibility = View.VISIBLE
//                        }
//                        else -> {
//                            nav_view.visibility = View.GONE
//                        }
                    }
                }
            }
        }
    }
}