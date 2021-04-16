package com.appchef.dishapp.model.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.appchef.dishapp.model.entitie.FavDish
import java.util.concurrent.Flow

@Dao
interface FavDishDao {

    @Insert
    suspend fun insertFavDishDetails(favDish: FavDish)

    @Query("SELECT * FROM FAV_DISH_TABLE ORDER BY ID")
    fun getAllDishesList(): kotlinx.coroutines.flow.Flow<List<FavDish>>
}

// Suspend using Coroutines to do things on the background thread.
// The Dao will be a interface
// Here we will do the basic CRUD functions.