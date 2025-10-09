package com.example.project_flam

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.SurfaceTexture
import android.hardware.camera2.*
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.HandlerThread
import android.provider.MediaStore
import android.util.Log
import android.util.Size
import android.view.Surface
import android.view.TextureView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.project_flam.databinding.ActivityMainBinding
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var textureView: TextureView
    
    // Camera2 API related variables
    private var cameraDevice: CameraDevice? = null
    private var cameraCaptureSession: CameraCaptureSession? = null
    private lateinit var cameraManager: CameraManager
    private var cameraId: String = ""
    private lateinit var captureRequestBuilder: CaptureRequest.Builder
    
    // Background thread for camera operations
    private var backgroundThread: HandlerThread? = null
    private var backgroundHandler: Handler? = null
    
    // FPS tracking
    private var frameCount = 0
    private var lastFpsUpdateTime = System.currentTimeMillis()
    private var currentFps = 0.0
    
    // Edge detection toggle
    private var edgeDetectionEnabled = false
    
    // Constants are defined in the single companion object at the bottom

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        textureView = binding.textureView
        cameraManager = getSystemService(CAMERA_SERVICE) as CameraManager

        // Display OpenCV info
        val nativeMessage = stringFromJNI()
        val openCVVersion = getOpenCVVersion()
        val matTest = testOpenCVMat()
        
        binding.sampleText.text = "OpenCV: $openCVVersion | Mat Test: $matTest"

        // Set up toggle for edge detection
        binding.edgeDetectionToggle.setOnCheckedChangeListener { _, isChecked ->
            edgeDetectionEnabled = isChecked
            binding.sampleText.text = "Mode: ${if (isChecked) "Edge Detection" else "Raw Camera"} | OpenCV: $openCVVersion"
            Log.d(TAG, "Edge detection ${if (isChecked) "enabled" else "disabled"}")
        }

        // Set up save frame button
        binding.saveFrameButton.setOnClickListener {
            saveCurrentFrame()
        }

        // Set up camera preview
        setupCamera()
    }

    private fun setupCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            setupTextureView()
        } else {
            requestCameraPermission()
        }
    }

    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.CAMERA),
            CAMERA_PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        
        when (requestCode) {
            CAMERA_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    setupTextureView()
                    Toast.makeText(this, "Camera permission granted", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Camera permission is required for camera preview", Toast.LENGTH_LONG).show()
                    finish()
                }
            }
        }
    }

    private fun setupTextureView() {
        textureView.surfaceTextureListener = object : TextureView.SurfaceTextureListener {
            override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
                Log.d(TAG, "SurfaceTexture available: ${width}x${height}")
                openCamera()
            }

            override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {
                Log.d(TAG, "SurfaceTexture size changed: ${width}x${height}")
            }

            override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
                Log.d(TAG, "SurfaceTexture destroyed")
                closeCamera()
                return true
            }

            override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {
                // Called when the SurfaceTexture is updated - camera frames are ready here
                updateFPS()
            }
        }
    }

    private fun openCamera() {
        try {
            // Get the first available camera (usually back camera)
            cameraId = cameraManager.cameraIdList[0]
            
            Log.d(TAG, "Opening camera: $cameraId")
            
            val stateCallback = object : CameraDevice.StateCallback() {
                override fun onOpened(camera: CameraDevice) {
                    Log.d(TAG, "Camera opened successfully")
                    cameraDevice = camera
                    createCameraPreviewSession()
                }

                override fun onDisconnected(camera: CameraDevice) {
                    Log.w(TAG, "Camera disconnected")
                    camera.close()
                    cameraDevice = null
                }

                override fun onError(camera: CameraDevice, error: Int) {
                    Log.e(TAG, "Camera error: $error")
                    camera.close()
                    cameraDevice = null
                    runOnUiThread {
                        Toast.makeText(this@MainActivity, "Camera error: $error", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            cameraManager.openCamera(cameraId, stateCallback, backgroundHandler)
            
        } catch (e: CameraAccessException) {
            Log.e(TAG, "Camera access exception: ${e.message}")
            Toast.makeText(this, "Camera access error: ${e.message}", Toast.LENGTH_SHORT).show()
        } catch (e: SecurityException) {
            Log.e(TAG, "Security exception: ${e.message}")
            Toast.makeText(this, "Camera permission error: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun createCameraPreviewSession() {
        try {
            val surfaceTexture = textureView.surfaceTexture
            
            // Set the default buffer size to be the size of camera preview we want
            val characteristics = cameraManager.getCameraCharacteristics(cameraId)
            val map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
            val previewSize = map?.getOutputSizes(SurfaceTexture::class.java)?.get(0) ?: Size(1920, 1080)
            
            surfaceTexture?.setDefaultBufferSize(previewSize.width, previewSize.height)
            val surface = Surface(surfaceTexture)

            captureRequestBuilder = cameraDevice?.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)!!
            captureRequestBuilder.addTarget(surface)

            cameraDevice?.createCaptureSession(
                listOf(surface),
                object : CameraCaptureSession.StateCallback() {
                    override fun onConfigured(session: CameraCaptureSession) {
                        Log.d(TAG, "Camera capture session configured")
                        cameraCaptureSession = session
                        updatePreview()
                    }

                    override fun onConfigureFailed(session: CameraCaptureSession) {
                        Log.e(TAG, "Camera capture session configuration failed")
                        Toast.makeText(this@MainActivity, "Camera configuration failed", Toast.LENGTH_SHORT).show()
                    }
                },
                backgroundHandler
            )
            
        } catch (e: CameraAccessException) {
            Log.e(TAG, "Camera access exception in preview session: ${e.message}")
            Toast.makeText(this, "Preview session error: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updatePreview() {
        try {
            captureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO)
            
            cameraCaptureSession?.setRepeatingRequest(
                captureRequestBuilder.build(),
                null,
                backgroundHandler
            )
            
            Log.d(TAG, "Camera preview started")
            runOnUiThread {
                binding.sampleText.text = binding.sampleText.text.toString() + " | Camera: Active"
            }
            
        } catch (e: CameraAccessException) {
            Log.e(TAG, "Camera access exception in update preview: ${e.message}")
        }
    }

    private fun closeCamera() {
        try {
            cameraCaptureSession?.close()
            cameraCaptureSession = null
            
            cameraDevice?.close()
            cameraDevice = null
            
            Log.d(TAG, "Camera closed")
        } catch (e: Exception) {
            Log.e(TAG, "Error closing camera: ${e.message}")
        }
    }

    private fun startBackgroundThread() {
        backgroundThread = HandlerThread("CameraBackground").also { it.start() }
        backgroundHandler = Handler(backgroundThread?.looper!!)
        Log.d(TAG, "Background thread started")
    }

    private fun stopBackgroundThread() {
        backgroundThread?.quitSafely()
        try {
            backgroundThread?.join()
            backgroundThread = null
            backgroundHandler = null
            Log.d(TAG, "Background thread stopped")
        } catch (e: InterruptedException) {
            Log.e(TAG, "Background thread interruption: ${e.message}")
        }
    }

    override fun onResume() {
        super.onResume()
        startBackgroundThread()
        
        if (textureView.isAvailable) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                openCamera()
            }
        } else {
            setupTextureView()
        }
    }

    override fun onPause() {
        closeCamera()
        stopBackgroundThread()
        super.onPause()
    }

    private fun updateFPS() {
        frameCount++
        val currentTime = System.currentTimeMillis()
        val elapsedTime = currentTime - lastFpsUpdateTime
        
        // Update FPS every second
        if (elapsedTime >= 1000) {
            currentFps = (frameCount * 1000.0) / elapsedTime
            frameCount = 0
            lastFpsUpdateTime = currentTime
            
            runOnUiThread {
                binding.fpsCounter.text = String.format("FPS: %.1f", currentFps)
            }
        }
    }

    private fun saveCurrentFrame() {
        val bitmap = textureView.bitmap ?: run {
            Toast.makeText(this, "No frame available to save", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            // Save to Pictures directory
            val fileName = "frame_${System.currentTimeMillis()}.jpg"
            val savedFile: File?

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // Use MediaStore for Android 10+
                val contentValues = ContentValues().apply {
                    put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
                    put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                    put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                }

                val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                uri?.let {
                    val outputStream: OutputStream? = contentResolver.openOutputStream(it)
                    outputStream?.use { stream ->
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 95, stream)
                    }
                    Toast.makeText(this, "Frame saved: $fileName", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "Frame saved to Pictures: $fileName")
                }
            } else {
                // Legacy approach for older Android versions
                val picturesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                savedFile = File(picturesDir, fileName)
                FileOutputStream(savedFile).use { stream ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 95, stream)
                }
                Toast.makeText(this, "Frame saved: ${savedFile.absolutePath}", Toast.LENGTH_SHORT).show()
                Log.d(TAG, "Frame saved: ${savedFile.absolutePath}")
            }

            // Also save a copy as "frame.jpg" for web viewer
            val webFrameFile = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "frame.jpg")
            FileOutputStream(webFrameFile).use { stream ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 95, stream)
            }
            Log.d(TAG, "Web viewer frame saved: ${webFrameFile.absolutePath}")
            
        } catch (e: Exception) {
            Log.e(TAG, "Error saving frame: ${e.message}")
            Toast.makeText(this, "Error saving frame: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * A native method that is implemented by the 'project_flam' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String
    
    /**
     * Native method to get OpenCV version
     */
    external fun getOpenCVVersion(): String
    
    /**
     * Native method to test OpenCV Mat creation
     */
    external fun testOpenCVMat(): Int

    /**
     * Native method to process frame with edge detection
     * @param enableEdgeDetection true to apply Canny edge detection, false for raw frame
     * @return processed frame data (placeholder for future implementation)
     */
    external fun processFrame(enableEdgeDetection: Boolean): Int

    companion object {
        const val CAMERA_PERMISSION_REQUEST_CODE = 100
        const val TAG = "MainActivity"

        // Used to load the 'project_flam' library on application startup.
        init {
            System.loadLibrary("project_flam")
        }
    }
}