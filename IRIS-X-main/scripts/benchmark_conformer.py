#!/usr/bin/env python3
"""
IRIS-MX Conformer Encoder FLOPs & Throughput Analyzer
"""

import time
import math

def calculate_conformer_flops(seq_len: int = 100, d_model: int = 256):
    attn_flops = 2 * seq_len * seq_len * d_model
    conv_flops = 2 * seq_len * d_model * 31
    total_gflops = (attn_flops + conv_flops) / 1e9
    return total_gflops

def run_benchmark():
    print("[BENCHMARK] Evaluating Conformer Encoder FLOPs & Memory Throughput...")
    start = time.perf_counter()
    gflops = calculate_conformer_flops(seq_len=200, d_model=512)
    elapsed = (time.perf_counter() - start) * 1000.0
    print(f"Total Theoretical GFLOPs: {gflops:.4f} GFLOPs | Benchmark Time: {elapsed:.3f} ms")

if __name__ == "__main__":
    run_benchmark()
