package com.x201harsh.IRISMX.audio

import android.util.Log
import kotlin.math.max
import kotlin.math.sqrt

class IrisAcousticNoiseSuppressor {

    private val noiseFloorEstimate = FloatArray(512) { 0.01f }
    private var adaptationFactor = 0.95f

    fun suppressNoise(fftMagnitude: FloatArray): FloatArray {
        val output = FloatArray(fftMagnitude.size)

        for (i in fftMagnitude.indices) {
            val mag = fftMagnitude[i]
            // Update noise floor estimate during quiet segments
            if (mag < noiseFloorEstimate[i] * 1.5f) {
                noiseFloorEstimate[i] = noiseFloorEstimate[i] * adaptationFactor + mag * (1.0f - adaptationFactor)
            }

            // Spectral Subtraction Wiener Filter
            val snr = max(0f, mag - noiseFloorEstimate[i] * 2.0f)
            val gain = snr / (snr + noiseFloorEstimate[i])
            output[i] = mag * gain
        }
        return output
    }

    companion object {
        private const val TAG = "IrisAcousticNoiseSuppressor"
    }
}
