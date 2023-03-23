package com.ht117.selfie.ui.screen.selfie

import android.content.ContentValues
import android.content.pm.PackageManager
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.ht117.selfie.R
import com.ht117.selfie.databinding.FragmentSelfieBinding
import com.ht117.selfie.ext.viewBinding
import com.ht117.selfie.ui.screen.base.BaseScreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.Executors


class SelfieScreen: BaseScreen(R.layout.fragment_selfie) {

    private var count = 0
    private val cameraLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            startCamera()
        } else {
            navigateBack()
        }
    }

    private val binding by viewBinding(FragmentSelfieBinding::bind)
    private var imgCapture: ImageCapture? = ImageCapture.Builder().build()
    override fun onStart() {
        super.onStart()
        if (hasCameraPermissions()) {
            startCamera()
        } else {
            cameraLauncher.launch(android.Manifest.permission.CAMERA)
        }
    }

    private fun startCamera() {
        context?.let { ctx ->
            val camProviderFuture = ProcessCameraProvider.getInstance(ctx)
            camProviderFuture.addListener({
                val camProvider = camProviderFuture.get()
                val preview = Preview.Builder()
                    .build()
                    .also {
                        it.setSurfaceProvider(binding.previewCam.surfaceProvider)
                    }

                val camSelector = CameraSelector.DEFAULT_FRONT_CAMERA
                try {
                    camProvider.unbindAll()
                    camProvider.bindToLifecycle(this, camSelector, preview, imgCapture)

                    lifecycleScope.launch {
                        for (i in 0 until TOTAL_TIME) {
                            binding.tvText.text = getString(R.string.take_photo_desc, (TOTAL_TIME - i).toString())
                            if (i % 2 == 0) {
                                takePhoto()
                            }
                            delay(SECOND)
                        }
                        binding.tvText.setText(R.string.processing)
                    }
                } catch (exp: Exception) {
                    exp.printStackTrace()
                }
            }, ContextCompat.getMainExecutor(ctx))
        }
    }

    private fun takePhoto() {
        val name = "selfie_${System.currentTimeMillis()}"
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/Selfie")
            }
        }

        val outputOptions = ImageCapture.OutputFileOptions
            .Builder(
                requireContext().contentResolver,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues)
            .build()

        imgCapture?.takePicture(
            outputOptions,
            Executors.newSingleThreadExecutor(),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    exc.printStackTrace()
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    count++
                    if (count == 3) {
                        viewLifecycleOwner.lifecycleScope.launch {
                            navigateBack()
                        }
                    }
                }
            }
        )
    }

    private fun hasCameraPermissions(): Boolean {
        return context?.let { ctx ->
            REQUIRED_PERMISSIONS.all {
                ContextCompat.checkSelfPermission(
                    ctx,
                    it
                ) == PackageManager.PERMISSION_GRANTED
            }
        }?: false
    }

    companion object {
        private const val TOTAL_TIME = 5
        private const val SECOND = 1_000L

        private val REQUIRED_PERMISSIONS = mutableListOf(
            android.Manifest.permission.CAMERA
        ).apply {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }
    }
}
