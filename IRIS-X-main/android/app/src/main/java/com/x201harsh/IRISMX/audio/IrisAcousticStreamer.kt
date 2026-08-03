package com.x201harsh.IRISMX.audio

import android.annotation.SuppressLint
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.util.Log
import kotlinx.coroutines.*
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.math.sqrt

class IrisAcousticStreamer {

    private const val SAMPLE_RATE = 44100
    private const val CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO
    private const val AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT

    private val isRecording = AtomicBoolean(false)
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var audioRecord: AudioRecord? = null

    @SuppressLint("MissingPermission")
    fun startStreaming(onAudioChunkProcessed: (FloatArray, Float) -> Unit) {
        if (isRecording.getAndSet(true)) return

        val minBufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT)
        val bufferSize = minBufferSize.coerceAtLeast(2048)

        try {
            audioRecord = AudioRecord(
                MediaRecorder.AudioSource.MIC,
                SAMPLE_RATE,
                CHANNEL_CONFIG,
                AUDIO_FORMAT,
                bufferSize
            )

            audioRecord?.startRecording()
            Log.i(TAG, "IrisAcousticStreamer recording started with buffer size: $bufferSize")

            scope.launch {
                val shortBuffer = ShortArray(1024)
                val floatBuffer = FloatArray(1024)

                while (isRecording.get() && isActive) {
                    val readCount = audioRecord?.read(shortBuffer, 0, shortBuffer.size) ?: 0
                    if (readCount > 0) {
                        var sumSq = 0f
                        for (i in 0 until readCount) {
                            val normalized = shortBuffer[i] / 32768.0f
                            floatBuffer[i] = normalized
                            sumSq += normalized * normalized
                        }
                        val rms = sqrt(sumSq / readCount)
                        onAudioChunkProcessed(floatBuffer, rms)
                    }
                    delay(10)
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to start AudioRecord acoustic streamer", e)
            isRecording.set(false)
        }
    }

    fun stopStreaming() {
        isRecording.set(false)
        try {
            audioRecord?.stop()
            audioRecord?.release()
            audioRecord = null
            Log.i(TAG, "IrisAcousticStreamer stopped")
        } catch (e: Exception) {
            Log.e(TAG, "Error stopping AudioRecord", e)
        }
    }

    companion object {
        private const val TAG = "IrisAcousticStreamer"
    }
}
