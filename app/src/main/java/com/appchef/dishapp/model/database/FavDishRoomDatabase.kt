package com.appchef.dishapp.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.appchef.dishapp.model.entitie.FavDish

@Database(entities = [FavDish::class], version = 1)
abstract class FavDishRoomDatabase : RoomDatabase(){

    abstract fun favDishDao(): FavDishDao

    companion object{
        @Volatile
        private var INSTANCE : FavDishRoomDatabase? = null

        fun getDatabase(context: Context) : FavDishRoomDatabase {
            // If instance is null create one else return it.

            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FavDishRoomDatabase::class.java,
                    "fav_dish_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

// Singelton class
// The Database class will be abstract.
// returning the instance here.