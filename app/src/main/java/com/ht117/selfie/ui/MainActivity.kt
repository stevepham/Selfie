package com.ht117.selfie.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.ht117.selfie.R
import com.ht117.selfie.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding?.root)
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }

    override fun onBackPressed() {
    }
}
