#ifndef IRIS_SIMD_MATRIX_HPP
#define IRIS_SIMD_MATRIX_HPP

#include <cstddef>

namespace iris {
namespace simd {

class IrisSimdMatrixAccelerator {
public:
    static float dotProductSimd(const float* vecA, const float* vecB, size_t length);
    static void matrixVectorMultiplySimd(const float* matrix, const float* vecIn, float* vecOut, size_t rows, size_t cols);
};

} // namespace simd
} // namespace iris

#endif // IRIS_SIMD_MATRIX_HPP
