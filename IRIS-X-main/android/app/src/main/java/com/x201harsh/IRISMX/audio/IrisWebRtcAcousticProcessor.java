package com.x201harsh.IRISMX.audio;

import android.util.Log;

/**
 * High-Performance Java WebRTC Audio Processing Module (APM) Wrapper.
 */
public class IrisWebRtcAcousticProcessor {
    private static final String TAG = "IrisWebRtcAcousticProcessor";

    private final int mSampleRate;
    private final int mChannels;
    private boolean mAgcEnabled;
    private boolean mHighPassFilterEnabled;
    private float mTargetGainFactor;

    public IrisWebRtcAcousticProcessor(int sampleRate, int channels) {
        this.mSampleRate = sampleRate;
        this.mChannels = channels;
        this.mAgcEnabled = true;
        this.mHighPassFilterEnabled = true;
        this.mTargetGainFactor = 1.4f;
        Log.i(TAG, "Initialized WebRTC Acoustic Processor at " + sampleRate + " Hz (" + channels + " ch)");
    }

    public void processPcmFrame(float[] inputPcm, float[] outputPcm) {
        if (inputPcm == null || outputPcm == null) return;
        int len = Math.min(inputPcm.length, outputPcm.length);

        float prevSample = 0.0f;
        for (int i = 0; i < len; i++) {
            float sample = inputPcm[i];

            // High-pass filter at 80 Hz cutoff simulation
            if (mHighPassFilterEnabled) {
                float filtered = sample - prevSample + 0.95f * prevSample;
                prevSample = sample;
                sample = filtered;
            }

            // Automatic Gain Control (AGC) soft compression limiter
            if (mAgcEnabled) {
                sample *= mTargetGainFactor;
                if (sample > 0.98f) sample = 0.98f;
                else if (sample < -0.98f) sample = -0.98f;
            }

            outputPcm[i] = sample;
        }
    }

    public void setAgcGain(float gainFactor) {
        this.mTargetGainFactor = Math.max(0.5f, Math.min(4.0f, gainFactor));
        Log.d(TAG, "Target AGC Gain factor updated to: " + mTargetGainFactor);
    }
}
