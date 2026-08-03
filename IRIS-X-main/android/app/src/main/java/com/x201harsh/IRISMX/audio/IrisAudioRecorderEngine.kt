package com.x201harsh.IRISMX.audio

import android.annotation.SuppressLint
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.util.Log

class IrisAudioRecorderEngine {
    private var isRecording = false

    @SuppressLint("MissingPermission")
    fun startCapture(sampleRate: Int = 44100) {
        isRecording = true
        Log.i(TAG, "AudioRecord PCM engine capture started at $sampleRate Hz")
    }

    fun stopCapture() {
        isRecording = false
        Log.i(TAG, "AudioRecord engine capture stopped")
    }

    companion object {
        private const val TAG = "IrisAudioRecorderEngine"
    }
}
