package com.appchef.dishapp.view.activities

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.graphics.drawable.toBitmap
import com.appchef.dishapp.R
import com.appchef.dishapp.databinding.ActivityAddUpdateDishBinding
import com.appchef.dishapp.databinding.CustomDialogImageSelectionBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.karumi.dexter.Dexter
import com.karumi.dexter.DexterBuilder
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*

class AddUpdateDishActivity : AppCompatActivity(), View.OnClickListener {
    // END

    private lateinit var mBinding: ActivityAddUpdateDishBinding
    private var mImagePath: String = ""

    companion object {
        private const val CAMERA = 1
        private const val GALLERY = 2
        private const val IMAGE_DIRECTORY = "DishApp"
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
                    val galleryIntent = Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    )
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
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CAMERA) {
                // Setting the bitmap to the IV
                data?.let {
                    val thumbnail: Bitmap = data.extras!!.get("data") as Bitmap
                    mBinding.ivDishImage.setImageBitmap(thumbnail)

                    // Storing the Image to the Internal Storage
                    mImagePath = saveImagesToInternalStorage(thumbnail)

                }
            }

            if (requestCode == GALLERY) {
                // Setting the URI to the ImageView
                data?.let {

                    val selectedPhotoUri = data.data

                    // Loading Image by using Glide.
                    Glide.with(this)
                        .load(selectedPhotoUri)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .listener(object : RequestListener<Drawable> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any?,
                                target: Target<Drawable>?,
                                isFirstResource: Boolean
                            ): Boolean {
                                Log.e("TAG", "Error Loading the Image")
                                return false
                            }

                            override fun onResourceReady(
                                resource: Drawable?,
                                model: Any?,
                                target: Target<Drawable>?,
                                dataSource: DataSource?,
                                isFirstResource: Boolean
                            ): Boolean {
                                resource?.let {
                                    val bitmap: Bitmap = resource.toBitmap()
                                    mImagePath = saveImagesToInternalStorage(bitmap)
                                    Log.i("ImagePath", mImagePath)
                                }
                                return false
                            }

                        })
                        .centerCrop()
                        .into(mBinding.ivDishImage)


//                    mBinding.ivDishImage.setImageURI(selectedPhotoUri)
                }
            }
        } else if (requestCode == Activity.RESULT_CANCELED) {
            Log.e("Cancelled", "User Cancelled image selection")
        }
    }

    private fun saveImagesToInternalStorage(bitmap: Bitmap): String {
        // It will get the details(context) about our app i.e Dish App
        val wrapper = ContextWrapper(applicationContext)

        var file = wrapper.getDir(IMAGE_DIRECTORY, Context.MODE_PRIVATE)
        // Saving the Images to dir with random names
        file = File(file, "${UUID.randomUUID()}.jpg")

        // Converting the Bitmap
        try {
            val stream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
            // closing the stream is important
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return file.absolutePath
    }

}