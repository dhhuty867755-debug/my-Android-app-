#!/usr/bin/env bash
# IRIS-MX Python PyBind11 C++ Extension Builder

echo "========================================================="
echo "⚡ BUILDING PYBIND11 C++ EXTENSION MODULE"
echo "========================================================="

python setup.py build_ext --inplace

if [ $? -eq 0 ]; then
    echo "[SUCCESS] Built iris_native_py extension module cleanly."
    python test_python_bindings.py
else
    echo "[ERROR] Failed to build PyBind11 C++ extension."
    exit 1
fi
