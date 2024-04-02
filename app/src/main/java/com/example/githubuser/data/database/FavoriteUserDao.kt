package com.example.githubuser.data.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FavoriteUserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavoriteUser(user: FavoriteUser)

    @Query("SELECT * FROM FavoriteUser ORDER BY login ASC")
    fun getAllFavoriteUser(): LiveData<List<FavoriteUser>>

    @Query("SELECT * FROM FavoriteUser WHERE FavoriteUser.id = :id")
    fun getFavoriteUserById(id: Int): LiveData<FavoriteUser>

    @Query("DELETE FROM FavoriteUser WHERE FavoriteUser.id = :id")
    fun removeFavoriteUser(id: Int)
}