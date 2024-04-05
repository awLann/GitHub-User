package com.example.githubuser.ui.detail.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.data.response.UserResponse
import com.example.githubuser.databinding.FragmentFollowBinding

class FollowFragment : Fragment() {
    private lateinit var binding: FragmentFollowBinding
    private lateinit var viewModel: FollowViewModel
    private var position: Int = 0
    private var username: String? = null

    companion object {
        const val ARG_POSITION = "position"
        const val ARG_USERNAME = "username"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[FollowViewModel::class.java]

        showRecyclerList()

        viewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        arguments?.let {
            position = it.getInt(ARG_POSITION)
            username = it.getString(ARG_USERNAME)
        }

        if (position == 1) {
            viewModel.listFollowers.observe(viewLifecycleOwner) {
                setFollowData(it)
            }
            viewModel.getFollowers(username!!)
        } else {
            viewModel.listFollowing.observe(viewLifecycleOwner) {
                setFollowData(it)
            }
            viewModel.getFollowing(username!!)
        }
    }

    private fun showRecyclerList() {
        val layoutInflater = LinearLayoutManager(requireContext())
        binding.rvFollow.layoutManager = layoutInflater
    }

    private fun setFollowData(user: List<UserResponse>) {
        val adapter = FollowAdapter()
        adapter.submitList(user)
        binding.rvFollow.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}