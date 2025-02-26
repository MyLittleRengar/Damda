package com.project.damda

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.project.damda.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setBottomNavigationView()

        if(savedInstanceState == null) {
            binding.mainBottomNav.selectedItemId = R.id.fragment_home
        }
    }

    private fun setBottomNavigationView() {
        binding.mainBottomNav.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.fragment_home -> {
                    supportFragmentManager.beginTransaction().replace(R.id.main_container, HomeFragment()).commit()
                    true
                }
                R.id.fragment_recipe -> {
                    supportFragmentManager.beginTransaction().replace(R.id.main_container, RecipeFragment()).commit()
                    true
                }
                R.id.fragment_bookmark -> {
                    supportFragmentManager.beginTransaction().replace(R.id.main_container, BookmarkFragment()).commit()
                    true
                }
                R.id.fragment_setting -> {
                    supportFragmentManager.beginTransaction().replace(R.id.main_container, SettingFragment()).commit()
                    true
                }
                else -> false
            }
        }
    }
}