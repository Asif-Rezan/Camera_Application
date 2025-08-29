package com.asifrezan.camera_application.ui


import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.asifrezan.camera_application.databinding.ActivityCameraBinding
import com.asifrezan.camera_application.models.PhotoType
import com.asifrezan.camera_application.utils.ImageSaver
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors



class CameraActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCameraBinding
    private var imageCapture: ImageCapture? = null
    private lateinit var cameraExecutor: ExecutorService
    private var currentPhotoType: PhotoType = PhotoType.ID_PHOTO

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val cameraGranted = permissions[Manifest.permission.CAMERA] == true
        if (cameraGranted) {
            startCamera()
        } else {
            Toast.makeText(this, "Camera permission is required", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cameraExecutor = Executors.newSingleThreadExecutor()

        // Check permissions
        checkPermissions()

        // Setup photo type selector
        binding.photoTypeSpinner.setItems(PhotoType.values().toList())
        binding.photoTypeSpinner.setOnItemSelectedListener { position, item ->
            currentPhotoType = item
            binding.overlayFrameView.setPhotoType(item)
            binding.aspectRatioText.text = item.aspectRatio
        }

        // Capture button
        binding.captureButton.setOnClickListener { takePhoto() }
    }

    private fun checkPermissions() {
        val permissions = mutableListOf(Manifest.permission.CAMERA)
        // Only request WRITE_EXTERNAL_STORAGE for Android 9 (API 28) or lower
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }

        val allGranted = permissions.all {
            ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
        }

        if (allGranted) {
            startCamera()
        } else {
            permissionLauncher.launch(permissions.toTypedArray())
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(binding.previewView.surfaceProvider)
            }

            // Image capture
            imageCapture = ImageCapture.Builder().build()

            // Select back camera
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
            } catch (exc: Exception) {
                Toast.makeText(this, "Failed to start camera", Toast.LENGTH_SHORT).show()
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return
        val outputOptions = ImageSaver.getOutputOptions(this, currentPhotoType)

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    Toast.makeText(this@CameraActivity, "Photo saved!", Toast.LENGTH_SHORT).show()
                }

                override fun onError(exc: ImageCaptureException) {
                    Toast.makeText(this@CameraActivity, "Error saving photo", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
}