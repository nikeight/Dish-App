package com.appchef.dishapp.view.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.appchef.dishapp.R
import com.appchef.dishapp.databinding.ActivityAddUpdateDishBinding

class AddUpdateDishActivity : AppCompatActivity(R.layout.activity_add_update_dish), View.OnClickListener {
    // Adding and updating the object here.

    private val mBinding: ActivityAddUpdateDishBinding = TODO()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupActionBar()

        // TODO Step 5: Assign the click event to the image button.
        // START
        mBinding.ivAddDishImage.setOnClickListener(this@AddUpdateDishActivity)
        // END
    }

    private fun setupActionBar() {
        setSupportActionBar(mBinding.toolbarAddDishActivity)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        // TODO Step 2: Replace the back button that we have generated.
//        // START
//        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
//        // END

        mBinding.toolbarAddDishActivity.setNavigationOnClickListener { onBackPressed() }
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.iv_add_dish_image -> {
                    Toast.makeText(
                        this@AddUpdateDishActivity,
                        "You have clicked on the ImageView.",
                        Toast.LENGTH_SHORT
                    ).show()

                    return
                }
            }
        }
    }
}