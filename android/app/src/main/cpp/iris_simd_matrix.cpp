#include "iris_simd_matrix.hpp"
#include <cmath>

#if defined(__ARM_NEON) || defined(__ARM_NEON__)
#include <arm_neon.h>
#endif

namespace iris {
namespace simd {

float IrisSimdMatrixAccelerator::dotProductSimd(const float* vecA, const float* vecB, size_t length) {
    float sum = 0.0f;
    size_t i = 0;

#if defined(__ARM_NEON) || defined(__ARM_NEON__)
    float32x4_t vSum = vdupq_n_f32(0.0f);
    for (; i + 4 <= length; i += 4) {
        float32x4_t vA = vld1q_f32(vecA + i);
        float32x4_t vB = vld1q_f32(vecB + i);
        vSum = vmlaq_f32(vSum, vA, vB);
    }
    float buffer[4];
    vst1q_f32(buffer, vSum);
    sum = buffer[0] + buffer[1] + buffer[2] + buffer[3];
#endif

    for (; i < length; ++i) {
        sum += vecA[i] * vecB[i];
    }
    return sum;
}

void IrisSimdMatrixAccelerator::matrixVectorMultiplySimd(const float* matrix, const float* vecIn, float* vecOut, size_t rows, size_t cols) {
    for (size_t r = 0; r < rows; ++r) {
        vecOut[r] = dotProductSimd(matrix + r * cols, vecIn, cols);
    }
}

} // namespace simd
} // namespace iris
