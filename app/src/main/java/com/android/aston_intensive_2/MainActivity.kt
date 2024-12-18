package com.android.aston_intensive_2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.android.aston_intensive_2.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var isSpinning: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding){
            rotateButton.setOnClickListener {
                if(!isSpinning) {
                    spinTheWheel()
                }
            }
        }

    }

    private fun spinTheWheel(){
        isSpinning = true

        val randomAngle = Random.nextInt(0, 720).toFloat()
        val startingAngle = binding.wheelOfColorsView.currentAngle
        rotatingWheel(startingAngle, randomAngle)
    }

    private fun rotatingWheel(startingAngle: Float, endingAngle: Float){
        val step = 10f
        var currentAngle = startingAngle

        lifecycleScope.launch {
            while (currentAngle < endingAngle){
                withContext(Dispatchers.IO){
                    binding.wheelOfColorsView.rotation()
                    currentAngle += step
                }
                kotlinx.coroutines.delay(20)
            }
            stopTheWheel(endingAngle)
        }
    }

    private fun stopTheWheel(endingAngle: Float) {
        isSpinning = false
        binding.wheelOfColorsView.settingCurrentAngle(endingAngle)
    }
}