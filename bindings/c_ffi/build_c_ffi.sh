#!/usr/bin/env bash
# IRIS-MX C FFI Shared Library Build Automation Script

echo "========================================================="
echo "⚡ COMPILING C FFI SHARED LIBRARY"
echo "========================================================="

make clean && make

if [ $? -eq 0 ]; then
    echo "[SUCCESS] Compiled C FFI library successfully."
else
    echo "[ERROR] C FFI compilation failed."
    exit 1
fi
