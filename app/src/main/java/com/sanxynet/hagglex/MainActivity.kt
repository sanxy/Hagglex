package com.sanxynet.hagglex

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.sanxynet.hagglex.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding

    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController
        binding.activityBottomNav.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->

            when (destination.id) {
                R.id.dashBoard -> binding.activityBottomNav.visibility = View.VISIBLE
                R.id.wallets -> binding.activityBottomNav.visibility = View.VISIBLE
                R.id.crypto -> binding.activityBottomNav.visibility = View.VISIBLE
                R.id.countrySearch -> binding.activityBottomNav.visibility = View.VISIBLE
                R.id.home -> binding.activityBottomNav.visibility = View.VISIBLE
                else -> binding.activityBottomNav.visibility = View.GONE
            }
        }


    }
}