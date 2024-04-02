package com.example.githubuser.ui.favorite

import androidx.recyclerview.widget.DiffUtil
import com.example.githubuser.data.database.FavoriteUser

class FavoriteDiffCallback(private val oldFavoriteList: List<FavoriteUser>, private val newFavoriteList: List<FavoriteUser>): DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldFavoriteList.size
    override fun getNewListSize(): Int = newFavoriteList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldFavoriteList[oldItemPosition].id == newFavoriteList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldFavoriteUser = oldFavoriteList[oldItemPosition]
        val newFavoriteUser = newFavoriteList[newItemPosition]
        return oldFavoriteUser.login == newFavoriteUser.login && oldFavoriteUser.htmlUrl == newFavoriteUser.htmlUrl
    }
}