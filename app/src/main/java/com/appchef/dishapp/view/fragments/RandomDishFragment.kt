package com.appchef.dishapp.view.fragments

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.appchef.dishapp.R
import com.appchef.dishapp.application.FavDishApplication
import com.appchef.dishapp.databinding.FragmentRandomDishBinding
import com.appchef.dishapp.model.entitie.FavDish
import com.appchef.dishapp.model.entitie.RandomDish
import com.appchef.dishapp.view.util.Constants
import com.appchef.dishapp.viewmodel.FavDishViewModel
import com.appchef.dishapp.viewmodel.FavDishViewModelFactory
import com.appchef.dishapp.viewmodel.NotificationsViewModel
import com.appchef.dishapp.viewmodel.RandomDishViewModel
import com.bumptech.glide.Glide

class RandomDishFragment : Fragment() {

    private var mBindingRandomDish: FragmentRandomDishBinding? = null

    private lateinit var mRandomDishViewModel: RandomDishViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBindingRandomDish = FragmentRandomDishBinding.inflate(inflater, container, false)
        return mBindingRandomDish!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mRandomDishViewModel = ViewModelProvider(this).get(RandomDishViewModel::class.java)
        mRandomDishViewModel.getRandomRecipeFromAPI()
        randomDishViewModelObserver()
    }

    private fun randomDishViewModelObserver() {
        mRandomDishViewModel.randomDishResponse.observe(viewLifecycleOwner,
            { randomDishResponse ->
                randomDishResponse?.let {
                    Log.i("DISH_API_DATA","$randomDishResponse")
//                    setRandomDishResponseInUI(randomDishResponse.recipes[0])
                }
            })

        mRandomDishViewModel.randomDishLoadingError.observe(viewLifecycleOwner,
            { dataError ->
                dataError?.let {
                    Log.e("Random Dish Api Error", "$dataError")
                }
            })

        mRandomDishViewModel.loadRandomDish.observe(viewLifecycleOwner,{
            loadRandomDish ->
            loadRandomDish?.let {
                Log.i("Random Dish API boolean","$loadRandomDish")
            }
        })
    }

    private fun setRandomDishResponseInUI(recipe: RandomDish.Recipe) {

        // Load the dish image in the ImageView.
        Glide.with(requireActivity())
            .load(recipe.image)
            .centerCrop()
            .into(mBindingRandomDish!!.ivDishImage)

        mBindingRandomDish!!.tvTitle.text = recipe.title

        // Default Dish Type
        var dishType: String = "other"

        if (recipe.dishTypes.isNotEmpty()) {
            dishType = recipe.dishTypes[0]
            mBindingRandomDish!!.tvType.text = dishType
        }

        // There is not category params present in the response so we will define it as Other.
        mBindingRandomDish!!.tvCategory.text = "Other"

        var ingredients = ""
        for (value in recipe.extendedIngredients) {

            if (ingredients.isEmpty()) {
                ingredients = value.original
            } else {
                ingredients = ingredients + ", \n" + value.original
            }
        }

        mBindingRandomDish!!.tvIngredients.text = ingredients

        // The instruction or you can say the Cooking direction text is in the HTML format so we will you the fromHtml to populate it in the TextView.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mBindingRandomDish!!.tvCookingDirection.text = Html.fromHtml(
                recipe.instructions,
                Html.FROM_HTML_MODE_COMPACT
            )
        } else {
            @Suppress("DEPRECATION")
            mBindingRandomDish!!.tvCookingDirection.text = Html.fromHtml(recipe.instructions)
        }

        mBindingRandomDish!!.tvCookingTime.text = recipe.readyInMinutes.toString()


        mBindingRandomDish!!.ivFavoriteDish.setOnClickListener {

            val randomDishDetails = FavDish(
                recipe.image,
                Constants.DISH_IMAGE_SOURCE_ONLINE,
                recipe.title,
                dishType,
                "Other",
                ingredients,
                recipe.readyInMinutes.toString(),
                recipe.instructions,
                true
            )

            val mFavDishViewModel: FavDishViewModel by viewModels {
                FavDishViewModelFactory((requireActivity().application as FavDishApplication).repository)
            }

            mFavDishViewModel.insert(randomDishDetails)

            mBindingRandomDish!!.ivFavoriteDish.setImageDrawable(
                ContextCompat.getDrawable(
                    requireActivity(),
                    R.drawable.ic_fav_selected_vector
                )
            )

            Toast.makeText(
                requireActivity(),
                resources.getString(R.string.dish_added),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mBindingRandomDish = null
    }
}