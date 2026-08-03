#include <pybind11/pybind11.h>
#include <pybind11/stl.h>
#include "../../android/app/src/main/cpp/iris_core_engine.h"
#include "../../android/app/src/main/cpp/iris_simd_matrix.hpp"
#include "../../android/app/src/main/cpp/iris_conformer_encoder.hpp"

namespace py = pybind11;

PYBIND11_MODULE(iris_native_py, m) {
    m.doc() = "IRIS-MX Low-Latency Native C++ Engine Python Bindings";

    py::class_<iris::core::IrisCoreEngine>(m, "IrisCoreEngine")
        .def(py::init<>())
        .def("initialize_engine", &iris::core::IrisCoreEngine::initializeEngine)
        .def("reset_state", &iris::core::IrisCoreEngine::resetState);

    py::class_<iris::speech::IrisConformerEncoder>(m, "IrisConformerEncoder")
        .def(py::init<int, int, int>(), py::arg("dModel") = 256, py::arg("numHeads") = 4, py::arg("kernelSize") = 31)
        .def("forward", &iris::speech::IrisConformerEncoder::forward);

    m.def("simd_dot_product", [](const std::vector<float>& a, const std::vector<float>& b) {
        if (a.size() != b.size()) throw std::runtime_error("Vector sizes must match");
        return iris::simd::IrisSimdMatrixAccelerator::dotProductSimd(a.data(), b.data(), a.size());
    }, "Compute SIMD accelerated vector dot product");
}
