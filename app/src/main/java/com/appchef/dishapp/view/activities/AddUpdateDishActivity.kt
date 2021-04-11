package com.appchef.dishapp.view.activities

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import com.appchef.dishapp.R
import com.appchef.dishapp.databinding.ActivityAddUpdateDishBinding
import com.appchef.dishapp.databinding.CustomDialogImageSelectionBinding
import com.karumi.dexter.Dexter
import com.karumi.dexter.DexterBuilder
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import java.util.*

class AddUpdateDishActivity : AppCompatActivity(), View.OnClickListener {
    // END

    private lateinit var mBinding: ActivityAddUpdateDishBinding

    companion object{
        private const val CAMERA = 1
        private const val GALLERY = 2
    }

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


    private fun customImageSelectionDialog() {
        val dialog = Dialog(this)
        val dialogBinding: CustomDialogImageSelectionBinding =
            CustomDialogImageSelectionBinding.inflate(layoutInflater)

        dialog.setContentView(dialogBinding.root)

        dialogBinding.cameraIv.setOnClickListener {
            // Ask permissions
            Dexter.withContext(this).withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                   report?.let {
                       if (report!!.areAllPermissionsGranted()) {
                        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                           startActivityForResult(intent, CAMERA)
                       }
                   }
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: MutableList<PermissionRequest>?,
                    p1: PermissionToken?
                ) {
                    // Showing the alert Dialog here.
                    showAlertDialogBox()
                }

            }).onSameThread().check()

            dialog.dismiss()
        }

        dialogBinding.galleryIv.setOnClickListener {
            // Ask for the gallery permission i.e Read and write.
            Dexter.withContext(this).withPermission(
                Manifest.permission.READ_EXTERNAL_STORAGE,
            ).withListener(object : PermissionListener {
                override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                    val galleryIntent = Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                        startActivityForResult(galleryIntent, GALLERY)
                }

                override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                    Toast("All permission denied")
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: PermissionRequest?,
                    p1: PermissionToken?
                ) {
                    showAlertDialogBox()
                }

            }).onSameThread().check()

            dialog.dismiss()
        }
        dialog.show()
    }

    private fun Toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showAlertDialogBox() {
        AlertDialog.Builder(this).setMessage("Go to settings to enable the permissions.")
            .setPositiveButton("Go to Settings") { _, _ ->
                try {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    e.printStackTrace()
                }
            }.setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK){
            if (requestCode ==  CAMERA){
                // Setting the bitmap to the IV
                data?.let {
                    val thumbnail : Bitmap = data.extras!!.get("data") as Bitmap
                    mBinding.ivDishImage.setImageBitmap(thumbnail)
                }
            }

            if (requestCode == GALLERY){
                // Setting the URI to the ImageView
                data?.let {

                    val selectedPhotoUri = data.data

                    mBinding.ivDishImage.setImageURI(selectedPhotoUri)
                }
            }
        }else if (requestCode == Activity.RESULT_CANCELED){
            Log.e("Cancelled","User Cancelled image selection")
        }
    }
}