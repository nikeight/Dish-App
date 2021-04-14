package com.appchef.dishapp.model.database

import androidx.room.Dao
import androidx.room.Insert
import com.appchef.dishapp.model.entitie.FavDish

@Dao
interface FavDishDao {

    @Insert
    suspend fun insertFavDishDetails(favDish: FavDish)
}

// Suspend using Coroutines to do things on the background thread.
// The Dao will be a interface
// Here we will do the basic CRUD functions.