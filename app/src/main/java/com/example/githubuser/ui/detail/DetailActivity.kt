package com.example.githubuser.ui.detail

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.githubuser.R
import com.example.githubuser.data.response.DetailUserResponse
import com.example.githubuser.data.response.UserResponse
import com.example.githubuser.databinding.ActivityDetailBinding
import com.example.githubuser.ui.detail.fragment.SectionsPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private val viewModel by viewModels<DetailViewModel>()

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

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}