package com.x201harsh.IRISMX.security;

import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Log;
import java.security.KeyStore;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

public class IrisZeroTrustKeyStore {
    private static final String TAG = "IrisZeroTrustKeyStore";
    private static final String KEY_ALIAS = "IRIS_MASTER_VOICE_KEY";
    private static final String ANDROID_KEYSTORE = "AndroidKeyStore";

    public IrisZeroTrustKeyStore() {
        try {
            initMasterKey();
        } catch (Exception e) {
            Log.e(TAG, "Error initializing Android KeyStore HSM", e);
        }
    }

    private void initMasterKey() throws Exception {
        KeyStore keyStore = KeyStore.getInstance(ANDROID_KEYSTORE);
        keyStore.load(null);

        if (!keyStore.containsAlias(KEY_ALIAS)) {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(
                KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEYSTORE
            );
            keyGenerator.init(
                new KeyGenParameterSpec.Builder(
                    KEY_ALIAS,
                    KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT
                )
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .setKeySize(256)
                .build()
            );
            keyGenerator.generateKey();
            Log.i(TAG, "Generated AES-256 GCM Master Security Key in Hardware KeyStore");
        }
    }

    public byte[] encryptPayload(byte[] plaintext) throws Exception {
        KeyStore keyStore = KeyStore.getInstance(ANDROID_KEYSTORE);
        keyStore.load(null);
        SecretKey secretKey = (SecretKey) keyStore.getKey(KEY_ALIAS, null);

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] iv = cipher.getIV();
        byte[] ciphertext = cipher.doFinal(plaintext);

        byte[] combined = new byte[iv.length + ciphertext.length];
        System.arraycopy(iv, 0, combined, 0, iv.length);
        System.arraycopy(ciphertext, 0, combined, iv.length, ciphertext.length);
        return combined;
    }
}
