package com.example.githubuser.ui.favorite

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubuser.data.database.FavoriteUser
import com.example.githubuser.databinding.ItemUserBinding
import com.example.githubuser.ui.detail.DetailActivity

class FavoriteUserAdapter: RecyclerView.Adapter<FavoriteUserAdapter.ViewHolder>() {
    private val listUserFavorite = ArrayList<FavoriteUser>()

    class ViewHolder(private val binding: ItemUserBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: FavoriteUser) {
            Glide.with(itemView.context)
                .load(item.avatarUrl)
                .circleCrop()
                .into(binding.ivAvatar)
            binding.tvName.text = item.login

            binding.listUser.setOnClickListener {
                val intent = Intent(it.context, DetailActivity::class.java)
                intent.putExtra(DetailActivity.INTENT_PARCELABLE, item.login)
                it.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listUserFavorite[position])
    }

    override fun getItemCount(): Int = listUserFavorite.size

    fun setFavoriteUserList(listUserFavorite: List<FavoriteUser>) {
        val diffCallback = FavoriteDiffCallback(this.listUserFavorite, listUserFavorite)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.listUserFavorite.clear()
        this.listUserFavorite.addAll(listUserFavorite)
        diffResult.dispatchUpdatesTo(this)
    }
}