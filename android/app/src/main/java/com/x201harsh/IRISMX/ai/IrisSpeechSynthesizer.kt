package com.x201harsh.IRISMX.ai

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.util.Log

class IrisSpeechSynthesizer {
    private var audioTrack: AudioTrack? = null
    private var isSynthesizing = false

    fun initAudioTrack(sampleRate: Int = 24000) {
        val minBufSize = AudioTrack.getMinBufferSize(
            sampleRate, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT
        )
        audioTrack = AudioTrack.Builder()
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                    .build()
            )
            .setAudioFormat(
                AudioFormat.Builder()
                    .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                    .setSampleRate(sampleRate)
                    .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                    .build()
            )
            .setBufferSizeInBytes(minBufSize * 2)
            .build()

        audioTrack?.play()
        Log.i(TAG, "AudioTrack TTS Player initialized at $sampleRate Hz")
    }

    fun playPcmChunk(pcmData: ByteArray) {
        audioTrack?.write(pcmData, 0, pcmData.size)
    }

    fun stop() {
        audioTrack?.stop()
        audioTrack?.release()
        audioTrack = null
    }

    companion object {
        private const val TAG = "IrisSpeechSynthesizer"
    }
}
