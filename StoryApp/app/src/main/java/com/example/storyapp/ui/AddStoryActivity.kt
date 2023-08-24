package com.example.storyapp.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.storyapp.*
import com.example.storyapp.databinding.ActivityAddStoryBinding
import com.example.storyapp.model.AddStoryViewModel
import com.example.storyapp.model.ViewModelFactory
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class AddStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddStoryBinding
    private val addStoryViewModel: AddStoryViewModel by viewModels {
        ViewModelFactory.getInstance(context = this)
    }
    private lateinit var result: Bitmap
    private var getFileGallery: File? = null
    private var getFileCamera: File? = null
    private var isBack: Boolean = false

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    "Tidak mendapatkan permission.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        binding.btnCamera.setOnClickListener { startCameraX() }
        binding.btnGallery.setOnClickListener { startGallery() }

        initObserve()

        binding.uploadStory.setOnClickListener {
            val description = binding.descriptionText.text.toString()
            if (description.isNotEmpty()) {
                loadingState(true)
                uploadStory(description)
            } else {
                val msg = getString(R.string.description)
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun uploadStory(description: String) {
        if (getFileGallery != null) {
            val file = reduceFileImageGallery(getFileGallery as File)

            val requestDescription = description.toRequestBody("text/plain".toMediaType())
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                file.name,
                requestImageFile
            )

            addStoryViewModel.getUserToken().observe(this) { token ->
                if (token != null) {
                    addStoryViewModel.uploadStory(
                        imageMultipart,
                        requestDescription,
                        token
                    )
                } else {
                    val msg = getString(R.string.upload_fail)
                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
                }
            }
        } else if (getFileCamera != null) {
            val file = reduceFileImageCamera(getFileCamera as File, isBack)

            val requestDescription = description.toRequestBody("text/plain".toMediaType())
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                file.name,
                requestImageFile
            )

            addStoryViewModel.getUserToken().observe(this) { token ->
                if (token != null) {
                    addStoryViewModel.uploadStory(
                        imageMultipart,
                        requestDescription,
                        token
                    )
                } else {
                    val msg = getString(R.string.upload_fail)
                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            val msg = getString(R.string.upload_fail)
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }
    }

    private fun initObserve() {
        addStoryViewModel.error.observe(this) { error ->
            if (error == false) {
                Toast.makeText(this, getString(R.string.upload_success), Toast.LENGTH_SHORT).show()
                loadingState(false)
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                loadingState(false)
                val msg = getString(R.string.upload_fail)
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg = result.data?.data as Uri

            val myFile = uriToFile(selectedImg, this)

            getFileGallery = myFile

            binding.previewImage.setImageURI(selectedImg)
        }
    }

    private fun startCameraX() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            @Suppress("DEPRECATION")
            val myFile = it.data?.getSerializableExtra("picture") as File
            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean

            getFileCamera = myFile
            isBack = isBackCamera
            result = rotateBitmap(
                BitmapFactory.decodeFile(myFile.path),
                isBackCamera
            )
            binding.previewImage.setImageBitmap(result)
        }
    }

    private fun loadingState(state: Boolean) {
        binding.loadingIcon.visibility = if (state) View.VISIBLE else View.GONE
    }

    companion object {
        const val CAMERA_X_RESULT = 200
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}