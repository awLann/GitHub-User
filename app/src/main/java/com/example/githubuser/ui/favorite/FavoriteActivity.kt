package com.example.githubuser.ui.favorite

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.databinding.ActivityFavoriteBinding
import com.example.githubuser.ui.factory.FavoriteViewModelFactory

class FavoriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var viewModel: FavoriteViewModel
    private lateinit var adapter: FavoriteUserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory = FavoriteViewModelFactory.getInstance(this@FavoriteActivity.application)
        viewModel = ViewModelProvider(this@FavoriteActivity, factory)[FavoriteViewModel::class.java]

        showRecyclerList()

        viewModel.getAllFavoriteUser().observe(this@FavoriteActivity) { favoriteUser ->
            if (favoriteUser != null) {
                adapter.setFavoriteUserList(favoriteUser)
            }
        }
    }

    private fun showRecyclerList() {
        adapter = FavoriteUserAdapter()
        binding.rvFavUser.layoutManager = LinearLayoutManager(this)
        binding.rvFavUser.setHasFixedSize(false)
        binding.rvFavUser.adapter = this.adapter
    }
}