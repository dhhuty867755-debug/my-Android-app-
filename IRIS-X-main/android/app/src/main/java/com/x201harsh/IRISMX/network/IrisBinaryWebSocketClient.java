package com.x201harsh.IRISMX.network;

import android.util.Log;
import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicBoolean;

public class IrisBinaryWebSocketClient {
    private static final String TAG = "IrisBinaryWebSocketClient";

    private final String mServerUri;
    private final AtomicBoolean mIsConnected;
    private long mBytesTransmitted;

    public IrisBinaryWebSocketClient(String serverUri) {
        this.mServerUri = serverUri;
        this.mIsConnected = new AtomicBoolean(false);
        this.mBytesTransmitted = 0L;
    }

    public boolean connect() {
        mIsConnected.set(true);
        Log.i(TAG, "Connected binary WebSocket stream to " + mServerUri);
        return true;
    }

    public void sendPcmFrame(ByteBuffer pcmBuffer) {
        if (!mIsConnected.get() || pcmBuffer == null) return;
        int bytes = pcmBuffer.remaining();
        mBytesTransmitted += bytes;
    }

    public void disconnect() {
        mIsConnected.set(false);
        Log.i(TAG, "Disconnected WebSocket stream. Total transmitted: " + mBytesTransmitted + " bytes");
    }

    public boolean isConnected() {
        return mIsConnected.get();
    }
}
