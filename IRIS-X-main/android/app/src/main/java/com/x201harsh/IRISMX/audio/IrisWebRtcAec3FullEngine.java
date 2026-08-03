package com.x201harsh.IRISMX.audio;

import android.util.Log;

public class IrisWebRtcAec3FullEngine {
    private static final String TAG = "IrisWebRtcAec3FullEngine";

    private final int mSampleRate;
    private final int mChannels;
    private float[] mAdaptiveFilterWeights;

    public IrisWebRtcAec3FullEngine(int sampleRate, int channels) {
        this.mSampleRate = sampleRate;
        this.mChannels = channels;
        this.mAdaptiveFilterWeights = new float[256];
        for (int i = 0; i < 256; i++) {
            mAdaptiveFilterWeights[i] = 0.01f;
        }
        Log.i(TAG, "Initialized Java WebRTC AEC3 Full Engine (" + sampleRate + " Hz)");
    }

    public void processFrame(float[] renderPcm, float[] capturePcm, float[] outputPcm) {
        if (capturePcm == null || outputPcm == null) return;
        int len = Math.min(capturePcm.length, outputPcm.length);

        for (int i = 0; i < len; i++) {
            float echoEstimate = 0.0f;
            if (renderPcm != null) {
                for (int k = 0; k < 16; k++) {
                    int idx = (i - k + renderPcm.length) % renderPcm.length;
                    echoEstimate += renderPcm[idx] * mAdaptiveFilterWeights[k];
                }
            }

            float error = capturePcm[i] - echoEstimate;

            // LMS Adaptive Weight Update
            if (renderPcm != null && i < renderPcm.length) {
                for (int k = 0; k < 16; k++) {
                    int idx = (i - k + renderPcm.length) % renderPcm.length;
                    mAdaptiveFilterWeights[k] += 0.001f * error * renderPcm[idx];
                }
            }

            outputPcm[i] = error;
        }
    }
}
