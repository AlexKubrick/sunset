package com.bignerdranch.android.sunset

import android.animation.AnimatorSet
import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AccelerateInterpolator
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import com.bignerdranch.android.sunset.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val blueSkyColor: Int by lazy {
        ContextCompat.getColor(this, R.color.blue_sky)
    }
    private val sunsetSkyColor: Int by lazy {
        ContextCompat.getColor(this, R.color.sunset_sky)
    }
    private val nightSkyColor: Int by lazy {
        ContextCompat.getColor(this, R.color.night_sky)
    }
    // chapter 25. challenge 1.
    private var isSunset = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // chapter 25. challenge 1.
        binding.scene.setOnClickListener {
            if (isSunset) {
                startAnimation()
            } else {
                reverseAnimation()
            }
            isSunset = !isSunset
        }
    }

    private fun startAnimation() {
        val sunYStart = binding.sun.top.toFloat()
        val sunYEnd = binding.sky.height.toFloat()

        val haloYStart = binding.halo.top.toFloat()
        val haloYEnd = binding.sky.height.toFloat()

        val heightAnimator = ObjectAnimator
            .ofFloat(binding.sun, "y", sunYStart, sunYEnd)
            .setDuration(3000)
        heightAnimator.interpolator = AccelerateInterpolator()

        val heightHaloAnimator = ObjectAnimator
            .ofFloat(binding.halo, "y", haloYStart, haloYEnd)
            .setDuration(3000)
        heightHaloAnimator.interpolator = AccelerateInterpolator()

        val haloSpinAnimator = ObjectAnimator
            .ofFloat(binding.halo, "rotation", 0f, 360f)
            .setDuration(3000)

        val sunPulseAnimator = ValueAnimator.ofFloat(1f, 1.1f, 1f)
        sunPulseAnimator.addUpdateListener {
            val value = it.animatedValue as Float
            binding.sun.scaleX = value
        }
        sunPulseAnimator.repeatMode = ValueAnimator.REVERSE
        sunPulseAnimator.repeatCount = ObjectAnimator.INFINITE
        sunPulseAnimator.duration = 500L
        sunPulseAnimator.start()

        val sunsetSkyAnimator = ObjectAnimator
            .ofInt(binding.sky, "backgroundColor", blueSkyColor, sunsetSkyColor)
            .setDuration(3000)
        sunsetSkyAnimator.setEvaluator(ArgbEvaluator())

        val nightSkyAnimator = ObjectAnimator
            .ofInt(binding.sky, "backgroundColor", sunsetSkyColor, nightSkyColor)
            .setDuration(1500)
        nightSkyAnimator.setEvaluator(ArgbEvaluator())

        val animatorSet = AnimatorSet()
        animatorSet.play(heightAnimator)
            .with(heightHaloAnimator)
            .with(haloSpinAnimator)
            .with(sunsetSkyAnimator)
            .before(nightSkyAnimator)
        animatorSet.start()
    }

    // chapter 25. challenge 1.
    private fun reverseAnimation() {
        val sunYStart = binding.sun.top.toFloat()
        val sunYEnd = binding.sky.bottom.toFloat()

        val haloYStart = binding.halo.top.toFloat()
        val haloYEnd = binding.sky.height.toFloat()

        val heightAnimator = ObjectAnimator
            .ofFloat(binding.sun, "y", sunYEnd, sunYStart)
            .setDuration(3000)
        heightAnimator.interpolator = AccelerateInterpolator()

        val heightHaloAnimator = ObjectAnimator
            .ofFloat(binding.halo, "y", haloYEnd, haloYStart)
            .setDuration(3000)
        heightHaloAnimator.interpolator = AccelerateInterpolator()

        val sunsetSkyAnimator = ObjectAnimator
            .ofInt(binding.sky, "backgroundColor", nightSkyColor, sunsetSkyColor)
            .setDuration(1500)
        sunsetSkyAnimator.setEvaluator(ArgbEvaluator())

        val normalSkyAnimator = ObjectAnimator
            .ofInt(binding.sky, "backgroundColor", sunsetSkyColor, blueSkyColor)
            .setDuration(3000)
        normalSkyAnimator.setEvaluator(ArgbEvaluator())

        val animatorSet = AnimatorSet()
        animatorSet.play(heightAnimator)
            .with(heightHaloAnimator)
            .with(sunsetSkyAnimator)
            .before(normalSkyAnimator)
        animatorSet.start()
    }
}

