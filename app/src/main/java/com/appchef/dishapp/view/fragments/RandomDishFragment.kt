package com.appchef.dishapp.view.fragments

import android.os.Bundle
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

class RandomDishFragment : Fragment() {

   private var mBindingRandomDish : FragmentRandomDishBinding? = null

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
       mBindingRandomDish = FragmentRandomDishBinding.inflate(inflater,container,false)

        return mBindingRandomDish!!.root
    }

    override fun onDestroy() {
        super.onDestroy()
        mBindingRandomDish = null
    }
}