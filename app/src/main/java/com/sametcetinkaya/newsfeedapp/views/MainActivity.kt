package com.sametcetinkaya.newsfeedapp.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.sametcetinkaya.newsfeedapp.R
import com.sametcetinkaya.newsfeedapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        navController = navHostFragment.navController
        /*setSupportActionBar(binding.toolbar)
        setupActionBarWithNavController(navController)*/

        //binding.toolbar.setupWithNavController()

        binding.bottomNav.setupWithNavController(navController)
        binding.bottomNav.setOnItemReselectedListener { /***/ }
    }
}