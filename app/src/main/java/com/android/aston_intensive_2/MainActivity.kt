package com.android.aston_intensive_2

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
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
    private var restoredAngle: Float = 0f
    private var displayedText: String? = null
    private var imageUrl: String? = null
    private var seekBarProgress: Int = 50
    private var isShowingImage: Boolean = false

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putFloat("restoredAngle", restoredAngle)
        outState.putString("displayedText", displayedText)
        outState.putString("restoredImageUrl", imageUrl)
        outState.putInt("seekBarProgress", seekBarProgress)
        outState.putBoolean("isShowingImage", isShowingImage)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        restoredAngle = savedInstanceState.getFloat("restoredAngle")
        binding.wheelOfColorsView.settingCurrentAngle(restoredAngle)
        displayedText = savedInstanceState.getString("displayedText")
        imageUrl = savedInstanceState.getString("restoredImageUrl")
        binding.sizeOfWheelSeekBar.progress = savedInstanceState.getInt("seekBarProgress")
        isShowingImage = savedInstanceState.getBoolean("isShowingImage", isShowingImage)

        if (displayedText != null && !isShowingImage) {
            drawText(displayedText!!)
        } else if (imageUrl != null) {
            loadImage()
        }
    }

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

            sizeOfWheelSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    val scale = progress / 50f
                    wheelOfColorsView.scaleX = scale
                    wheelOfColorsView.scaleY = scale
                    wheelOfColorsView.invalidate()
                    seekBarProgress = progress
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })

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
        with(binding) {
            colorTextView.text = text
            colorTextView.visibility = View.VISIBLE
            colorImageView.visibility = View.GONE
            displayedText = text
        }
    }

    private fun loadImage() {
        imageUrl = "https://placebeard.it/1280x720"
        lifecycleScope.launch {
            val bitmap = withContext(Dispatchers.IO) {
                val url = URL(imageUrl)
                BitmapFactory.decodeStream(url.openConnection().getInputStream())
            }

            with(binding) {
                colorImageView.setImageBitmap(bitmap)
                colorImageView.visibility = View.VISIBLE
                colorTextView.visibility = View.GONE
                isShowingImage = true
            }
        }
    }

    private fun resetUi() {
        with(binding) {
            colorImageView.setImageBitmap(null)
            colorTextView.text = null
            colorImageView.visibility = View.GONE
            colorTextView.visibility = View.GONE
            displayedText = null
            imageUrl = null
            seekBarProgress = 50
            sizeOfWheelSeekBar.progress = seekBarProgress
        }
    }
}