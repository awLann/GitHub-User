package com.example.githubuser.ui.theme

import android.os.Bundle
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.example.githubuser.databinding.ActivityThemeBinding
import com.example.githubuser.ui.factory.ThemeViewModelFactory

class ThemeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityThemeBinding
    private lateinit var viewModel: ThemeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityThemeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = ThemePreferences.getInstance(application.dataStore)
        viewModel = ViewModelProvider(this@ThemeActivity, ThemeViewModelFactory(pref))[ThemeViewModel::class.java]

        switchTheme(viewModel)
    }

    private fun switchTheme(viewModel: ThemeViewModel) {
        val switchTheme = binding.switchTheme

        switchTheme.setOnCheckedChangeListener { _:CompoundButton?, isChecked: Boolean ->
            this.viewModel.saveThemeSettings(isChecked)
        }

        this.viewModel.getThemeSettings().observe(this@ThemeActivity) { isDarkMode: Boolean ->
            if (isDarkMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                switchTheme.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                switchTheme.isChecked = false
            }
        }
    }
}