package com.x201harsh.IRISMX.core;

import android.util.Log;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * High-performance Java direct memory buffer manager for high-frequency NDK/JNI operations.
 */
public class IrisTensorBufferManager {
    private static final String TAG = "IrisTensorBufferManager";
    private static final int DEFAULT_BUFFER_CAPACITY_BYTES = 1024 * 64; // 64KB per slab
    private static final int POOL_SIZE = 16;

    private final ConcurrentLinkedQueue<ByteBuffer> mBufferPool;
    private static IrisTensorBufferManager sInstance;

    private IrisTensorBufferManager() {
        mBufferPool = new ConcurrentLinkedQueue<>();
        for (int i = 0; i < POOL_SIZE; i++) {
            ByteBuffer buf = ByteBuffer.allocateDirect(DEFAULT_BUFFER_CAPACITY_BYTES);
            buf.order(ByteOrder.nativeOrder());
            mBufferPool.offer(buf);
        }
        Log.i(TAG, "Initialized direct byte buffer pool with " + POOL_SIZE + " slabs of " + DEFAULT_BUFFER_CAPACITY_BYTES + " B");
    }

    public static synchronized IrisTensorBufferManager getInstance() {
        if (sInstance == null) {
            sInstance = new IrisTensorBufferManager();
        }
        return sInstance;
    }

    public ByteBuffer acquireBuffer() {
        ByteBuffer buf = mBufferPool.poll();
        if (buf == null) {
            buf = ByteBuffer.allocateDirect(DEFAULT_BUFFER_CAPACITY_BYTES);
            buf.order(ByteOrder.nativeOrder());
        } else {
            buf.clear();
        }
        return buf;
    }

    public void releaseBuffer(ByteBuffer buf) {
        if (buf != null && buf.isDirect() && mBufferPool.size() < POOL_SIZE) {
            buf.clear();
            mBufferPool.offer(buf);
        }
    }

    public FloatBuffer acquireFloatBuffer(int capacityFloats) {
        ByteBuffer byteBuffer = acquireBuffer();
        return byteBuffer.asFloatBuffer();
    }
}
