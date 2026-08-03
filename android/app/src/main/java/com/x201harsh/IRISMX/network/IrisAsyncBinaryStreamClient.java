package com.x201harsh.IRISMX.network;

import android.util.Log;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.Future;

public class IrisAsyncBinaryStreamClient {
    private static final String TAG = "IrisAsyncBinaryStreamClient";

    private AsynchronousSocketChannel mAsyncChannel;

    public void connect(String host, int port) {
        try {
            mAsyncChannel = AsynchronousSocketChannel.open();
            Future<Void> connectFuture = mAsyncChannel.connect(new InetSocketAddress(host, port));
            Log.i(TAG, "Asynchronous NIO socket connecting to " + host + ":" + port);
        } catch (IOException e) {
            Log.e(TAG, "Error connecting async socket", e);
        }
    }

    public void sendFrameAsync(ByteBuffer buffer) {
        if (mAsyncChannel == null || !mAsyncChannel.isOpen()) return;
        mAsyncChannel.write(buffer);
    }
}
