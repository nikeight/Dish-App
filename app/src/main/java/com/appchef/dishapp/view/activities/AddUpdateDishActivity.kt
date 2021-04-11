package com.appchef.dishapp.view.activities

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.appchef.dishapp.R
import com.appchef.dishapp.databinding.ActivityAddUpdateDishBinding
import com.appchef.dishapp.databinding.CustomDialogImageSelectionBinding

class AddUpdateDishActivity : AppCompatActivity(), View.OnClickListener {
    // END

    private lateinit var mBinding: ActivityAddUpdateDishBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityAddUpdateDishBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setupActionBar()
//
//        // TODO Step 5: Assign the click event to the image button.
//        // START
        mBinding.ivAddDishImage.setOnClickListener(this@AddUpdateDishActivity)
//        // END
    }

    // TODO Step 4: Override the onclick listener method.
    // START
    override fun onClick(v: View) {

        // TODO Step 6: Perform the action when user clicks on the addDishImage and show Toast message for now.
        // START
        when (v.id) {

            R.id.iv_add_dish_image -> {
                customImageSelectionDialog()
                return
            }
        }
        // END
    }
    // END

    /**
     * A function for ActionBar setup.
     */
    private fun setupActionBar() {
        setSupportActionBar(mBinding.toolbarAddDishActivity)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        // TODO Step 2: Replace the back button that we have generated.
        // START
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
        // END

        mBinding.toolbarAddDishActivity.setNavigationOnClickListener { onBackPressed() }
    }


    private fun customImageSelectionDialog(){
        val dialog = Dialog(this)
        val dialogBinding : CustomDialogImageSelectionBinding =
            CustomDialogImageSelectionBinding.inflate(layoutInflater)

        dialog.setContentView(dialogBinding.root)

        dialogBinding.cameraIv.setOnClickListener {
            Toast.makeText(this, "Opening Camera", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }

        dialogBinding.galleryIv.setOnClickListener {
            Toast.makeText(this, "Opening Gallery", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }

        dialog.show()
    }
}