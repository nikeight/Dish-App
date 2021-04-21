package com.appchef.dishapp.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.appchef.dishapp.R
import com.appchef.dishapp.application.FavDishApplication
import com.appchef.dishapp.databinding.FragmentFavoriteDishesBinding
import com.appchef.dishapp.model.entitie.FavDish
import com.appchef.dishapp.view.activities.MainActivity
import com.appchef.dishapp.view.adapter.FavDishAdapter
import com.appchef.dishapp.viewmodel.DashboardViewModel
import com.appchef.dishapp.viewmodel.FavDishViewModel
import com.appchef.dishapp.viewmodel.FavDishViewModelFactory

class FavoriteDishesFragment : Fragment() {

    private var mBinding: FragmentFavoriteDishesBinding? = null

    private val mFavDishViewModel: FavDishViewModel by viewModels {
        FavDishViewModelFactory((requireActivity().application as FavDishApplication).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentFavoriteDishesBinding.inflate(inflater, container, false)
        return mBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mFavDishViewModel.favoriteDishes.observe(viewLifecycleOwner) { dishes ->
            dishes.let {

                mBinding!!.rvFavoriteDishesList.layoutManager =
                    GridLayoutManager(requireActivity(), 2)
                val adapter = FavDishAdapter(this@FavoriteDishesFragment)
                mBinding!!.rvFavoriteDishesList.adapter = adapter

                if (it.isNotEmpty()) {
                    mBinding!!.rvFavoriteDishesList.visibility = View.VISIBLE
                    mBinding!!.tvNoFavoriteDishesAvailable.visibility = View.GONE

                    adapter.dishesList(it)
                } else {
                    mBinding!!.rvFavoriteDishesList.visibility = View.GONE
                    mBinding!!.tvNoFavoriteDishesAvailable.visibility = View.VISIBLE
                }
            }
        }
    }

    fun dishDetails(favDish: FavDish) {
        // direct to the other fragments.
        findNavController().navigate(
            FavoriteDishesFragmentDirections
                .actionNavigationFavoriteDishesToNavigationDishDetails(
                    favDish
                )
        )

        if (requireActivity() is MainActivity) {
            (activity as MainActivity).hideBtmNavigation()
        }
    }

    override fun onResume() {
        // Displaying the btm nav with animation whenever we came
        // Back to the FavDish Fragment.
        super.onResume()
        if (requireActivity() is MainActivity){
            (activity as MainActivity).showBTmNavigation()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }
}