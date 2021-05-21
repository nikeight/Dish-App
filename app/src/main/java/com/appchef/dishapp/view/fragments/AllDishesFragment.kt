package com.appchef.dishapp.view.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.appchef.dishapp.R
import com.appchef.dishapp.application.FavDishApplication
import com.appchef.dishapp.databinding.DialogCustomListBinding
import com.appchef.dishapp.databinding.FragmentAllDishesBinding
import com.appchef.dishapp.model.entitie.FavDish
import com.appchef.dishapp.view.activities.AddUpdateDishActivity
import com.appchef.dishapp.view.activities.MainActivity
import com.appchef.dishapp.view.adapter.CustomListItemAdapter
import com.appchef.dishapp.view.adapter.FavDishAdapter
import com.appchef.dishapp.view.util.Constants
import com.appchef.dishapp.viewmodel.FavDishViewModel
import com.appchef.dishapp.viewmodel.FavDishViewModelFactory
import com.appchef.dishapp.viewmodel.HomeViewModel

class AllDishesFragment : Fragment() {

    private lateinit var mBinding: FragmentAllDishesBinding

    private lateinit var mFavDishAdapter: FavDishAdapter

    private lateinit var mCustomListDialog: Dialog

    // ViewModel object.
    private val mFavDishViewModel: FavDishViewModel by viewModels {
        FavDishViewModelFactory((requireActivity().application as FavDishApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding =
            FragmentAllDishesBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set the LayoutManager to Grid Format.
        mBinding.rvDishesList.layoutManager = GridLayoutManager(requireActivity(), 2)
        mFavDishAdapter = FavDishAdapter(this@AllDishesFragment)


        mBinding.rvDishesList.adapter = mFavDishAdapter

        mFavDishViewModel.allDishesList.observe(viewLifecycleOwner) { dishes ->
            dishes.let {
                for (item in it) {
                    if (it.isNotEmpty()) {

                        mBinding.rvDishesList.visibility = View.VISIBLE
                        mBinding.tvNoDishesAddedYet.visibility = View.GONE

                        // Sending the List item from here.
                        mFavDishAdapter.dishesList(it)
                    } else {

                        mBinding.rvDishesList.visibility = View.GONE
                        mBinding.tvNoDishesAddedYet.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    fun moveToDishDetails(favDish: FavDish) {
        findNavController().navigate(
            AllDishesFragmentDirections.actionNavigationAllDishesToNavigationDishDetails(
                favDish
            )
        )

        // hiding the btm nav after the click
        if (requireActivity() is MainActivity) {
            (activity as MainActivity?)!!.hideBtmNavigation()
        }
    }

    private fun filterDishesListDialog() {
        mCustomListDialog = Dialog(requireActivity())
        val binding: DialogCustomListBinding = DialogCustomListBinding.inflate(layoutInflater)

        mCustomListDialog.setContentView(binding.root)
        binding.tvTitle.text = resources.getString(R.string.title_select_dish_category)
        val dishType = Constants.dishTypes()
        dishType.add(0, Constants.ALL_ITEMS)

        // Setting up the list
        binding.rvList.layoutManager = LinearLayoutManager(requireActivity())
        val adapter = CustomListItemAdapter(requireActivity(), this, dishType, Constants.FILTER_SELECTION)

        // Setting adapter and showing it.
        binding.rvList.adapter = adapter
        mCustomListDialog.show()
    }

    override fun onResume() {
        super.onResume()
        // show the btm nav whenever we came to the fragment again.
        if (requireActivity() is MainActivity) {
            (activity as MainActivity?)!!.showBTmNavigation()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_all_dishes, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_add_dish -> {
                startActivity(Intent(requireActivity(), AddUpdateDishActivity::class.java))
                return true
            }

            R.id.action_filter_dish -> {
                filterDishesListDialog()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun deleteDish(dish: FavDish) {
        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle("Delete the Dish from the List")
        builder.setMessage("Are you sure you want to delete this dish item?")
        builder.setPositiveButton("Delete") { dialogInterface, _ ->
            mFavDishViewModel.delete(dish)
            dialogInterface.dismiss()
        }

        builder.setNegativeButton("NO") { dialogInterface, _ ->
            dialogInterface.dismiss()
        }

        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    fun filterSelection(filterItemSelected : String){
        mCustomListDialog.dismiss()
        Log.i("Filter Selected Item",filterItemSelected)


    }
}