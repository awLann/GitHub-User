package com.example.githubuser.ui.detail

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.githubuser.R
import com.example.githubuser.data.database.FavoriteUser
import com.example.githubuser.data.response.DetailUserResponse
import com.example.githubuser.data.response.UserResponse
import com.example.githubuser.databinding.ActivityDetailBinding
import com.example.githubuser.ui.detail.fragment.SectionsPagerAdapter
import com.example.githubuser.ui.factory.FavoriteViewModelFactory
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var viewModel: DetailViewModel

    private var buttonStatus: Boolean = false
    private lateinit var favoriteUser: FavoriteUser
    private var detailUser = DetailUserResponse()

    companion object {
        const val INTENT_PARCELABLE = "OBJECT_INTENT"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.followers,
            R.string.following
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val application = FavoriteViewModelFactory.getInstance(this@DetailActivity.application)
        viewModel = ViewModelProvider(this@DetailActivity, application).get(DetailViewModel::class.java)

        val user = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(INTENT_PARCELABLE, UserResponse::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(INTENT_PARCELABLE)
        }

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }

        var currUser = user?.login.toString()
        viewModel.getUser(currUser)
        viewModel.listDetail.observe(this) {
            showUser(it)
            setFavoriteUser(it)
        }
        setViewPager(currUser)
    }

    private fun showUser(listUser: DetailUserResponse) {
        Glide.with(this@DetailActivity)
            .load(listUser.avatarUrl)
            .circleCrop()
            .into(binding.ivAvatar)
        binding.tvName.text = listUser.name
        binding.tvUsername.text = listUser.login
        binding.tvFollower.text = "${listUser.followers} followers"
        binding.tvFollowing.text = "${listUser.following} following"
    }

    private fun setViewPager(currUser: String) {
        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        sectionsPagerAdapter.username = currUser
        binding.viewPager.adapter = sectionsPagerAdapter
        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }

    private fun insertDatabase(detailUserList: DetailUserResponse) {
        favoriteUser.let { favoriteUser ->
            favoriteUser.id = detailUserList.id!!
            favoriteUser.avatarUrl = detailUserList.avatarUrl
            favoriteUser.login = detailUserList.login
            favoriteUser.htmlUrl = detailUserList.htmlUrl
            viewModel.insertFavoriteUser(favoriteUser)
            Toast.makeText(this@DetailActivity, "User is added to Favorites", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setFavoriteUser(detailUserResponse: DetailUserResponse) {
        detailUser = detailUserResponse
        showUser(detailUserResponse)
        favoriteUser = FavoriteUser(detailUser.id!!, detailUser.avatarUrl, detailUser.login, detailUser.htmlUrl)

        viewModel.getAllFavoriteUser().observe(this) { favoriteUser ->
            if (favoriteUser != null) {
                for (data in favoriteUser) {
                    if (detailUserResponse.id!! == data.id) {
                        buttonStatus = true
                        binding.fabFavorite.setImageResource(R.drawable.ic_favorite)
                    }
                }
            }
        }

        binding.fabFavorite.setOnClickListener {
            if (!buttonStatus) {
                buttonStatus = true
                binding.fabFavorite.setImageResource(R.drawable.ic_favorite)
                insertDatabase(detailUserResponse)
            } else {
                buttonStatus = false
                binding.fabFavorite.setImageResource(R.drawable.ic_unfavorite)
                viewModel.deleteFavoriteUser(detailUserResponse.id!!)
                Toast.makeText(this@DetailActivity, "User is deleted from Favorites", Toast.LENGTH_SHORT).show()
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