#ifndef IRIS_TENSOR_MATH_HPP
#define IRIS_TENSOR_MATH_HPP

#include <vector>
#include <cstddef>

namespace iris {
namespace math {

class IrisTensorMath {
public:
    static void gemm(const float* A, const float* B, float* C, int M, int N, int K);
    static void softmax(float* input, size_t length);
    static void layerNorm(const float* input, float* output, const float* gamma, const float* beta, size_t size, float epsilon = 1e-5f);
};

} // namespace math
} // namespace iris

#endif // IRIS_TENSOR_MATH_HPP
