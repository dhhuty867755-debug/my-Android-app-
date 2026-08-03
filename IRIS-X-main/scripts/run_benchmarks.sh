#!/usr/bin/env bash
# IRIS-MX Comprehensive Latency Benchmark Suite

echo "========================================================="
echo "📊 RUNNING IRIS-MX MULTI-ENGINE LATENCY BENCHMARKS"
echo "========================================================="

echo "› Running Python Latency Benchmark..."
python scripts/benchmark_latency.py

echo "› Running Model Perplexity Evaluation..."
python scripts/eval_perplexity.py

echo "========================================================="
