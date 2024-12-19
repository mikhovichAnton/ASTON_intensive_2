package com.android.aston_intensive_2

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.android.aston_intensive_2.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var isSpinning: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            rotateButton.setOnClickListener {
                if (!isSpinning) {
                    spinTheWheel()
                }
            }

            resetButton.setOnClickListener {
                resetUi()
            }
        }

    }

    private fun spinTheWheel() {
        isSpinning = true

        val randomAngle = Random.nextInt(0, 720).toFloat()
        val startingAngle = binding.wheelOfColorsView.currentAngle
        rotatingWheel(startingAngle, randomAngle)
    }

    private fun rotatingWheel(startingAngle: Float, endingAngle: Float) {
        val step = 10f
        var currentAngle = startingAngle

        lifecycleScope.launch {
            while (currentAngle < endingAngle) {
                withContext(Dispatchers.Main) {
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
        with(binding) {
            wheelOfColorsView.settingCurrentAngle(endingAngle)
            when (wheelOfColorsView.getColorIndex()) {
                0 -> drawText("Text in red color")
                1 -> loadImage()
                2 -> drawText("Text in yellow color")
                3 -> loadImage()
                4 -> drawText("Text in blue color")
                5 -> loadImage()
                6 -> drawText("Text in violet color")
            }
        }
    }

    private fun drawText(text: String) {
        with(binding){
            textView.text = text
            textView.visibility = View.VISIBLE
            imageView.visibility = View.GONE
        }
    }

    private fun loadImage() {
        lifecycleScope.launch {
            val imageUrl = "https://placebeard.it/1280x720"
            val bitmap = withContext(Dispatchers.IO){
                val url = URL(imageUrl)
                BitmapFactory.decodeStream(url.openConnection().getInputStream())
            }
            with(binding){
                imageView.setImageBitmap(bitmap)
                imageView.visibility = View.VISIBLE
                textView.visibility = View.GONE
            }

        }
    }

    private fun resetUi() {
        with(binding) {
            imageView.setImageBitmap(null)
            textView.text = ""
            imageView.visibility = View.GONE
            textView.visibility = View.GONE
        }
    }
}