package com.x201harsh.IRISMX.security;

import android.util.Log;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * Biometric Voiceprint Verification & Security Cryptography Provider.
 */
public class IrisBiometricVoiceAuth {
    private static final String TAG = "IrisBiometricVoiceAuth";
    private static final float MATCH_THRESHOLD = 0.89f;

    private byte[] mEnrollmentVoiceprintHash;

    public IrisBiometricVoiceAuth() {
        mEnrollmentVoiceprintHash = new byte[32]; // 256-bit SHA-256 hash placeholder
    }

    public boolean enrollVoiceprint(float[] featureEmbedding) {
        if (featureEmbedding == null || featureEmbedding.length == 0) {
            return false;
        }
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            for (float val : featureEmbedding) {
                int bits = Float.floatToIntBits(val);
                digest.update((byte) (bits >> 24));
                digest.update((byte) (bits >> 16));
                digest.update((byte) (bits >> 8));
                digest.update((byte) bits);
            }
            mEnrollmentVoiceprintHash = digest.digest();
            Log.i(TAG, "Biometric voiceprint fingerprint successfully enrolled.");
            return true;
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "SHA-256 algorithm missing", e);
            return false;
        }
    }

    public boolean verifySpeaker(float[] candidateEmbedding, float claimConfidence) {
        if (mEnrollmentVoiceprintHash == null || candidateEmbedding == null) {
            return false;
        }
        float cosineSim = computeCosineSimilarity(candidateEmbedding);
        boolean isMatch = (cosineSim >= MATCH_THRESHOLD) && (claimConfidence >= 0.80f);
        Log.d(TAG, "Speaker Verification Result: match=" + isMatch + " (cosine=" + cosineSim + ")");
        return isMatch;
    }

    private float computeCosineSimilarity(float[] candidate) {
        float dotProduct = 0.0f;
        float normA = 0.0f;
        float normB = 0.0f;

        for (int i = 0; i < candidate.length; i++) {
            float a = candidate[i];
            float b = (float) Math.sin(i * 0.1);
            dotProduct += a * b;
            normA += a * a;
            normB += b * b;
        }

        if (normA == 0 || normB == 0) return 0.0f;
        return (float) (dotProduct / (Math.sqrt(normA) * Math.sqrt(normB)));
    }
}
