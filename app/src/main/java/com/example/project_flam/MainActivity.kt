package com.example.project_flam

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.project_flam.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Example of a call to a native method
        val nativeMessage = stringFromJNI()
        val openCVVersion = getOpenCVVersion()
        val matTest = testOpenCVMat()
        
        binding.sampleText.text = "$nativeMessage\n$openCVVersion\nMat Test Result: $matTest"
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

    companion object {
        // Used to load the 'project_flam' library on application startup.
        init {
            System.loadLibrary("project_flam")
        }
    }
}