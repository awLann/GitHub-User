package com.example.githubuser.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.R
import com.example.githubuser.data.response.UserResponse
import com.example.githubuser.databinding.ActivityMainBinding
import com.example.githubuser.ui.factory.ThemeViewModelFactory
import com.example.githubuser.ui.favorite.FavoriteActivity
import com.example.githubuser.ui.theme.ThemeActivity
import com.example.githubuser.ui.theme.ThemePreferences
import com.example.githubuser.ui.theme.dataStore

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = ThemePreferences.getInstance(application.dataStore)
        viewModel = ViewModelProvider(this@MainActivity, ThemeViewModelFactory(pref))[MainViewModel::class.java]

        showRecyclerList()

        searchUser(binding)

        topBar()

        switchTheme(viewModel)

        viewModel.listUser.observe(this) {
            setUserData(it)
        }

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    private fun showRecyclerList() {
        val layoutInflater = LinearLayoutManager(this)
        binding.rvUser.layoutManager = layoutInflater
    }

    private fun searchUser(binding: ActivityMainBinding) {
        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { textView, actionId, event ->
                    searchBar.setText(searchView.text)
                    searchView.hide()
                    viewModel.findUser(searchView.text.toString())
                    false
                }
        }
    }

    private fun setUserData(user: List<UserResponse>) {
        val adapter = UserAdapter(user)
        adapter.submitList(user)
        binding.rvUser.adapter = adapter
    }

    private fun topBar() {
        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu1 -> {
                    val favoriteIntent = Intent(this@MainActivity, FavoriteActivity::class.java)
                    startActivity(favoriteIntent)
                    true
                }
                R.id.menu2 -> {
                    val themeIntent = Intent(this@MainActivity, ThemeActivity::class.java)
                    startActivity(themeIntent)
                    true
                }
                else -> false
            }
        }
    }

    private fun switchTheme(viewModel: MainViewModel) {
        viewModel.getThemeSetting().observe(this@MainActivity) { isDarkMode: Boolean ->
            if (isDarkMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}