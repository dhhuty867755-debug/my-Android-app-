import importlib
import importlib.util
from setuptools import setup, Extension

pybind11_spec = importlib.util.find_spec("pybind11")
if pybind11_spec is not None:
    pybind11 = importlib.import_module("pybind11")
    pybind_include = pybind11.get_include()
else:
    pybind_include = "../../android/app/src/main/cpp"

ext_modules = [
    Extension(
        "iris_native_py",
        [
            "iris_native_py.cpp",
            "../../android/app/src/main/cpp/iris_core_engine.cpp",
            "../../android/app/src/main/cpp/iris_simd_matrix.cpp",
            "../../android/app/src/main/cpp/iris_conformer_encoder.cpp",
        ],
        include_dirs=[
            pybind_include,
            "../../android/app/src/main/cpp",
        ],
        language="c++",
        extra_compile_args=["-std=c++17", "-O3"],
    ),
]

setup(
    name="iris_native_py",
    version="2.4.0",
    author="IRIS-MX AI Platform",
    description="Python C++ bindings for low-latency native engine",
    ext_modules=ext_modules if pybind11_spec is not None else [],
)
