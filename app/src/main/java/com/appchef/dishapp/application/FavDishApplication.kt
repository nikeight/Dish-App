package com.appchef.dishapp.application

import android.app.Application
import com.appchef.dishapp.model.database.FavDishRepository
import com.appchef.dishapp.model.database.FavDishRoomDatabase

class FavDishApplication : Application() {

    // This class is generally to get the context.
    // It will init when needed not when the app is loaded.
    private val database by lazy {
        // Setting up the database first.
        FavDishRoomDatabase.getDatabase((this@FavDishApplication))
    }

    // Setting up the repository secondly.
    val repository by lazy {
        FavDishRepository(database.favDishDao())
    }
}