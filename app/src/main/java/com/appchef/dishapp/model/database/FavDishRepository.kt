package com.appchef.dishapp.model.database

import androidx.annotation.WorkerThread
import com.appchef.dishapp.model.entitie.FavDish
import java.util.concurrent.Flow

class FavDishRepository(private val favDishDao: FavDishDao) {

    // It will be in the worker Thread not in the main thread.
    @WorkerThread
    suspend fun insertFavDishData(favDish: FavDish) {
        favDishDao.insertFavDishDetails(favDish)
    }

    val allDishesList: kotlinx.coroutines.flow.Flow<List<FavDish>>
    = favDishDao.getAllDishesList()
}