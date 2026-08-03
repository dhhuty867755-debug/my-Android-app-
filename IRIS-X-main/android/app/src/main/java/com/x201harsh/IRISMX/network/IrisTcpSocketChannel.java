package com.x201harsh.IRISMX.network;

import android.util.Log;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.atomic.AtomicBoolean;

public class IrisTcpSocketChannel {
    private static final String TAG = "IrisTcpSocketChannel";

    private SocketChannel mChannel;
    private final AtomicBoolean mIsConnected;
    private final String mHost;
    private final int mPort;

    public IrisTcpSocketChannel(String host, int port) {
        this.mHost = host;
        this.mPort = port;
        this.mIsConnected = new AtomicBoolean(false);
    }

    public boolean connect() {
        try {
            mChannel = SocketChannel.open();
            mChannel.configureBlocking(false);
            mChannel.connect(new InetSocketAddress(mHost, mPort));
            mIsConnected.set(true);
            Log.i(TAG, "NIO Socket Channel connected to " + mHost + ":" + mPort);
            return true;
        } catch (IOException e) {
            Log.e(TAG, "Failed to connect socket channel", e);
            mIsConnected.set(false);
            return false;
        }
    }

    public int sendBinaryChunk(ByteBuffer buffer) {
        if (!mIsConnected.get() || mChannel == null) return -1;
        try {
            int bytesWritten = mChannel.write(buffer);
            return bytesWritten;
        } catch (IOException e) {
            Log.e(TAG, "Error writing to socket channel", e);
            return -1;
        }
    }

    public void close() {
        mIsConnected.set(false);
        if (mChannel != null) {
            try {
                mChannel.close();
                Log.i(TAG, "Socket channel closed");
            } catch (IOException e) {
                Log.e(TAG, "Error closing channel", e);
            }
        }
    }
}
