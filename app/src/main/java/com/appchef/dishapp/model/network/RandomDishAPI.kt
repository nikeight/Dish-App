package com.appchef.dishapp.model.network

import com.appchef.dishapp.model.entitie.RandomDish
import com.appchef.dishapp.view.util.Constants
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

// TODO Step 3: Create a package called as "network" and create an interface to define the endpoint of the API that we are going to use.
// START
interface RandomDishAPI {

    @GET(Constants.API_ENDPOINT)
    fun getRandomDish(
        @Query(Constants.API_KEY) apiKey: String,
        @Query(Constants.LIMIT_LICENSE) limitLicense: Boolean,
        @Query(Constants.TAGS) tags: String,
        @Query(Constants.NUMBER) number: Int
    ) : Single<RandomDish.Recipes>
    // The Single class implements the Reactive Pattern for a single value response. Click on the class using the Ctrl + Left Mouse Click to know more.

}
