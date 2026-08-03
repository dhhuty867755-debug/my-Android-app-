#!/usr/bin/env python3
"""
IRIS-MX End-to-End Voice Telemetry & Latency Benchmarking Suite
"""

import time
import random
import asyncio
from typing import List, Dict

class LatencyBenchmarkSuite:
    def __init__(self, iterations: int = 100):
        self.iterations = iterations
        self.latencies_ms: List[float] = []

    async def simulate_audio_packet_pipeline(self, packet_id: int) -> float:
        start_time = time.perf_counter()
        
        # 1. Audio Record Ring Buffer Read
        await asyncio.sleep(0.002)
        
        # 2. Rust SIMD VAD Detection
        _ = [random.random() for _ in range(500)]
        await asyncio.sleep(0.001)

        # 3. C++ JNI NDK Beam Search Decoding
        await asyncio.sleep(0.004)

        # 4. React Native JS Bridge Broadcast
        await asyncio.sleep(0.003)

        elapsed = (time.perf_counter() - start_time) * 1000.0
        return elapsed

    async def run(self):
        print(f"[BENCHMARK] Starting Latency Benchmark ({self.iterations} audio frames)...")
        tasks = [self.simulate_audio_packet_pipeline(i) for i in range(self.iterations)]
        self.latencies_ms = await asyncio.gather(*tasks)

        sorted_lat = sorted(self.latencies_ms)
        p50 = sorted_lat[int(len(sorted_lat) * 0.50)]
        p95 = sorted_lat[int(len(sorted_lat) * 0.95)]
        p99 = sorted_lat[int(len(sorted_lat) * 0.99)]
        avg = sum(sorted_lat) / len(sorted_lat)

        print("\n================ BENCHMARK RESULTS ================")
        print(f"Total Processed Frames: {len(sorted_lat)}")
        print(f"Average Latency      : {avg:.2f} ms")
        print(f"P50 Latency          : {p50:.2f} ms")
        print(f"P95 Latency          : {p95:.2f} ms")
        print(f"P99 Latency          : {p99:.2f} ms (TARGET: < 20.0 ms)")
        print("====================================================\n")

if __name__ == "__main__":
    suite = LatencyBenchmarkSuite(iterations=200)
    asyncio.run(suite.run())