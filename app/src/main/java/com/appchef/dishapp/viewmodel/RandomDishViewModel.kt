package com.appchef.dishapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.appchef.dishapp.model.entitie.RandomDish
import com.appchef.dishapp.model.network.RandomDishApiService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers

// THis is the observer observing the observable objects like Single
class RandomDishViewModel: ViewModel(){

    private val randomRecipeApiService = RandomDishApiService()

    // Disposable basically is used to control the life cycle of an
    // Observable -> Like Single,Double,Triple (RxJava)
    private val compositeDisposable = CompositeDisposable()

    val loadRandomDish = MutableLiveData<Boolean>()
    val randomDishResponse = MutableLiveData<RandomDish.Recipes>()
    val randomDishLoadingError = MutableLiveData<Boolean>()

    fun getRandomDishFromAPI() {
        loadRandomDish.value = true

        // Subscribing on the new thread.
        // And Observing in the main thread.
        compositeDisposable.add(
            randomRecipeApiService.getRandomDish()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<RandomDish.Recipes>() {
                    override fun onSuccess(value: RandomDish.Recipes?) {
                        loadRandomDish.value = false
                        randomDishResponse.value = value
                        randomDishLoadingError.value = false
                    }

                    override fun onError(e: Throwable?) {
                        loadRandomDish.value = false
                        randomDishLoadingError.value = true
                        e!!.printStackTrace()
                    }
                })
        )
    }
}