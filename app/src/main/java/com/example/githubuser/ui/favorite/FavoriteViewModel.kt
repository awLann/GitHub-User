package com.example.githubuser.ui.favorite

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubuser.data.database.FavoriteUser
import com.example.githubuser.data.repository.FavoriteUserRepository

class FavoriteViewModel(application: Application) : ViewModel() {
    private val mFavoriteUserRepository: FavoriteUserRepository = FavoriteUserRepository(application)

    private val _listFavoriteUser = MutableLiveData<List<FavoriteUser>>()
    val listFavoriteUser: LiveData<List<FavoriteUser>> = _listFavoriteUser

    fun getAllFavoriteUser(): LiveData<List<FavoriteUser>> = mFavoriteUserRepository.getAllFavoriteUser()
}