package com.azhdar.myapplication5.presentation.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.azhdar.myapplication5.R
import com.azhdar.myapplication5.databinding.RecyclerViewBinding
import com.azhdar.myapplication5.presentation.favorites.FavoritesFragment
import com.azhdar.myapplication5.presentation.home.HomeFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: RecyclerViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = RecyclerViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Load HomeFragment by default
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, HomeFragment())
            .commit()

        // Setup bottom navigation
        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, HomeFragment())
                        .commit()
                    true
                }
                R.id.nav_favorites -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, FavoritesFragment())
                        .commit()
                    true
                }
                else -> false
            }
        }
    }
}