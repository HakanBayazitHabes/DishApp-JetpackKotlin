package com.tutorials.eu.favdish.model.database

import androidx.room.Dao
import androidx.room.Insert
import com.tutorials.eu.favdish.model.entities.FavDish

@Dao
interface FavDishDao {
    @Insert
    suspend fun insertFavDishDetails(favDish: FavDish)
}