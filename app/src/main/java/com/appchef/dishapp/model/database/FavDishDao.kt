package com.appchef.dishapp.model.database

import androidx.room.*
import com.appchef.dishapp.model.entitie.FavDish
import java.util.concurrent.Flow

// Suspend using Coroutines to do things on the background thread.
// The Dao will be a interface
// Here we will do the basic CRUD functions.

@Dao
interface FavDishDao {

    @Insert
    suspend fun insertFavDishDetails(favDish: FavDish)

    @Update
    suspend fun updateFavDishDetails(favDish: FavDish)

    @Delete
    suspend fun deleteFavDishDetails(favDish: FavDish)

    @Query("SELECT * FROM FAV_DISH_TABLE ORDER BY ID")
    fun getAllDishesList(): kotlinx.coroutines.flow.Flow<List<FavDish>>

    // TO get all the FavDishes
    @Query("SELECT * FROM fav_dish_table WHERE favourite_dish = 1")
    fun getFavoriteDishList() : kotlinx.coroutines.flow.Flow<List<FavDish>>

    // Filter
    @Query("SELECT * FROM FAV_DISH_TABLE WHERE type = :filterType")
    fun getFilteredDishesList(filterType : String): kotlinx.coroutines.flow.Flow<List<FavDish>>
}
