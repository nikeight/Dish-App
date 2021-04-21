package com.appchef.dishapp.viewmodel

import androidx.lifecycle.*
import com.appchef.dishapp.model.database.FavDishRepository
import com.appchef.dishapp.model.entitie.FavDish
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class FavDishViewModel(private val repository: FavDishRepository) : ViewModel() {

    fun insert(dish: FavDish) = viewModelScope.launch {
        // Launching the Coroutines scope and calling the Dao methods, indirectly
        repository.insertFavDishData(dish)
    }

    // live cycle aware.
    val allDishesList : LiveData<List<FavDish>> = repository.allDishesList.asLiveData()

    // update the favDish
    fun update(dish: FavDish) = viewModelScope.launch{
        repository.updateFavDishDetails(dish)
    }

    // to get the list of the favDishes
    val favoriteDishes: LiveData<List<FavDish>> = repository.favoriteDishes.asLiveData()

    fun delete(dish: FavDish) = viewModelScope.launch {
        repository.deleteFavDishDetails(dish)
    }
}

// Factory Builder setup
@Suppress("UNCHECKED_CAST")
class FavDishViewModelFactory(private val repository: FavDishRepository) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavDishViewModel::class.java)){
            return FavDishViewModel(repository) as T
        }
        throw IllegalArgumentException("Unkown ViewModel Class")
    }

}

