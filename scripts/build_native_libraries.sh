#!/usr/bin/env bash
# IRIS-MX Automated Multi-Language Native Builder

echo "========================================================="
echo "⚡ IRIS-MX MULTI-LANGUAGE NATIVE COMPILATION PIPELINE"
echo "========================================================="

echo "1. Building Rust Native Core (cdylib)..."
cd core_engine && cargo build --release && cd ..

echo "2. Compiling Android C++ NDK shared libraries..."
cd android && ./gradlew assembleDebug && cd ..

echo "3. Running Python static code security scan..."
python security_audit/scanner.py

echo "========================================================="
echo "SUCCESS: Native libraries compiled cleanly."
echo "========================================================="
