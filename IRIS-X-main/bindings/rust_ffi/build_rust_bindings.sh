#!/usr/bin/env bash
# IRIS-MX Rust FFI Build Script

echo "========================================================="
echo "⚡ BUILDING RUST CDYLIB SHARED LIBRARY"
echo "========================================================="

cargo build --release

if [ $? -eq 0 ]; then
    echo "[SUCCESS] Built Rust cdylib shared library."
else
    echo "[ERROR] Cargo build failed."
    exit 1
fi
