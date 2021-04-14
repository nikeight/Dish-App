package com.appchef.dishapp.model.database

import androidx.annotation.WorkerThread
import com.appchef.dishapp.model.entitie.FavDish

class FavDishRepository(private val favDishDao: FavDishDao) {

    // It will be in the worker Thread not in the main thread.
    suspend fun insertFavDishData(favDish : FavDish){
        @WorkerThread
        suspend fun insertFavDishDao(favDish: FavDish){
            favDishDao.insertFavDishDetails(favDish)
        }
    }
}