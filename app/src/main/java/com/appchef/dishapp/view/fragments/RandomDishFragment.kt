package com.appchef.dishapp.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.appchef.dishapp.R
import com.appchef.dishapp.databinding.FragmentRandomDishBinding
import com.appchef.dishapp.viewmodel.NotificationsViewModel
import com.appchef.dishapp.viewmodel.RandomDishViewModel

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
                    Log.i("Random Dish", "$randomDishResponse.recipes[0]")
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

    override fun onDestroy() {
        super.onDestroy()
        mBindingRandomDish = null
    }
}