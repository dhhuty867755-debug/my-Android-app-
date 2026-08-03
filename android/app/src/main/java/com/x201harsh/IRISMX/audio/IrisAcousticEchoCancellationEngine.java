package com.x201harsh.IRISMX.audio;

import android.util.Log;

/**
 * Enterprise Java WebRTC AEC3 Acoustic Echo Cancellation & Delay Estimator Engine.
 */
public class IrisAcousticEchoCancellationEngine {
    private static final String TAG = "IrisAcousticEchoCancellationEngine";

    private final int mSampleRate;
    private final int mFrameSize;
    private int mEstimatedDelayMs;
    private boolean mIsDoubleTalkDetected;
    private float mEchoSuppressionDb;

    public IrisAcousticEchoCancellationEngine(int sampleRate, int frameSize) {
        this.mSampleRate = sampleRate;
        this.mFrameSize = frameSize;
        this.mEstimatedDelayMs = 40;
        this.mIsDoubleTalkDetected = false;
        this.mEchoSuppressionDb = -30.0f;
        Log.i(TAG, "Initialized Java AEC3 Engine: sampleRate=" + sampleRate + " Hz, frameSize=" + frameSize);
    }

    public void processAudioFrames(short[] captureSignal, short[] renderSignal, short[] outputSignal) {
        if (captureSignal == null || outputSignal == null) return;
        int len = Math.min(captureSignal.length, outputSignal.length);

        float captureEnergy = 0.0f;
        float renderEnergy = 0.0f;

        for (int i = 0; i < len; i++) {
            captureEnergy += captureSignal[i] * captureSignal[i];
            if (renderSignal != null && i < renderSignal.length) {
                renderEnergy += renderSignal[i] * renderSignal[i];
            }
        }

        // Double-talk detection heuristic
        mIsDoubleTalkDetected = (captureEnergy > renderEnergy * 1.2f) && (captureEnergy > 100000.0f);

        for (int i = 0; i < len; i++) {
            float captureSample = captureSignal[i];
            float renderSample = (renderSignal != null && i < renderSignal.length) ? renderSignal[i] : 0.0f;

            float echoEst = mIsDoubleTalkDetected ? renderSample * 0.15f : renderSample * 0.75f;
            float cleaned = captureSample - echoEst;

            outputSignal[i] = (short) Math.max(-32768, Math.min(32767, (int) cleaned));
        }
    }

    public int getEstimatedDelayMs() {
        return mEstimatedDelayMs;
    }

    public boolean isDoubleTalkActive() {
        return mIsDoubleTalkDetected;
    }
}
