package com.appchef.dishapp.model.network

import com.appchef.dishapp.model.entitie.RandomDish
import com.appchef.dishapp.view.util.Constants
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import io.reactivex.rxjava3.core.Single
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RandomDishApiService {

    private val api = Retrofit.Builder().baseUrl(Constants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .build()
        .create(RandomDishAPI::class.java)

    fun getRandomDish() : Single<RandomDish.Recipe>{
        return api.getRandomDish(Constants.API_KEY_VALUE,
        Constants.LIMIT_LICENSE_VALUE,
        Constants.TAGS_VALUE,
        Constants.NUMBER_VALUE
        )
    }
}