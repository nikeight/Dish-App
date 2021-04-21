package com.appchef.dishapp.view.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.appchef.dishapp.R
import com.appchef.dishapp.databinding.ItemDishLayoutBinding
import com.appchef.dishapp.model.entitie.FavDish
import com.appchef.dishapp.view.activities.AddUpdateDishActivity
import com.appchef.dishapp.view.fragments.AllDishesFragment
import com.appchef.dishapp.view.fragments.FavoriteDishesFragment
import com.appchef.dishapp.view.util.Constants
import com.bumptech.glide.Glide

class FavDishAdapter(private val fragment: Fragment) :
    RecyclerView.Adapter<FavDishAdapter.ViewHolder>() {

    private var dishes: List<FavDish> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemDishLayoutBinding =
            ItemDishLayoutBinding.inflate(LayoutInflater.from(fragment.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val dish = dishes[position]

        // Load the dish image in the ImageView.
        Glide.with(fragment)
            .load(dish.image)
            .into(holder.ivDishImage)

        holder.tvTitle.text = dish.title

        holder.itemView.setOnClickListener {
            if (fragment is AllDishesFragment){
                fragment.moveToDishDetails(dish)
            }
            if (fragment is FavoriteDishesFragment){
                fragment.dishDetails(dish)
            }
        }

        holder.ibMore.setOnClickListener{
            val popupMenu = PopupMenu(fragment.context,holder.ibMore)
            popupMenu.menuInflater.inflate(R.menu.menu_adapter, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener {
                if (it.itemId == R.id.action_edit_dish){
                    val intent = Intent(fragment.requireActivity(),AddUpdateDishActivity::class.java)
                    intent.putExtra(Constants.EXTRA_DISH_DETAILS,dish)
                    fragment.requireActivity().startActivity(intent)
                }else if (it.itemId == R.id.action_delete_dish){
                    Log.i("EditMenu","You have clicked on Delete btn, ${dish.title}")
                }
                true
            }

            popupMenu.show()
        }

        if (fragment is AllDishesFragment){
            holder.ibMore.visibility = View.VISIBLE
        }else if (fragment is FavoriteDishesFragment){
            holder.ibMore.visibility = View.GONE
        }
    }

    /**
     * Gets the number of items in the list
     */
    override fun getItemCount(): Int {
        return dishes.size
    }

    // Create a function that will have the updated list of dishes that we will bind it to the adapter class.
    fun dishesList(list: List<FavDish>) {
        dishes = list
        notifyDataSetChanged()
    }

    class ViewHolder(view: ItemDishLayoutBinding) : RecyclerView.ViewHolder(view.root) {
        // Holds the TextView that will add each item to
        val ivDishImage = view.ivDishImage
        val tvTitle = view.tvDishTitle
        val ibMore = view.ibMore
    }
}
