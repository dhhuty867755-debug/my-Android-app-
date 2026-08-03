#include "iris_tensor_math.hpp"
#include <cmath>
#include <algorithm>

namespace iris {
namespace math {

void IrisTensorMath::gemm(const float* A, const float* B, float* C, int M, int N, int K) {
    for (int m = 0; m < M; ++m) {
        for (int n = 0; n < N; ++n) {
            float sum = 0.0f;
            for (int k = 0; k < K; ++k) {
                sum += A[m * K + k] * B[k * N + n];
            }
            C[m * N + n] = sum;
        }
    }
}

void IrisTensorMath::softmax(float* input, size_t length) {
    if (length == 0) return;
    float maxVal = input[0];
    for (size_t i = 1; i < length; ++i) {
        if (input[i] > maxVal) maxVal = input[i];
    }

    float sumExp = 0.0f;
    for (size_t i = 0; i < length; ++i) {
        input[i] = std::exp(input[i] - maxVal);
        sumExp += input[i];
    }

    for (size_t i = 0; i < length; ++i) {
        input[i] /= (sumExp > 0.0f ? sumExp : 1.0f);
    }
}

void IrisTensorMath::layerNorm(const float* input, float* output, const float* gamma, const float* beta, size_t size, float epsilon) {
    if (size == 0) return;
    float mean = 0.0f;
    for (size_t i = 0; i < size; ++i) mean += input[i];
    mean /= static_cast<float>(size);

    float var = 0.0f;
    for (size_t i = 0; i < size; ++i) {
        float diff = input[i] - mean;
        var += diff * diff;
    }
    var /= static_cast<float>(size);

    float stdDevInv = 1.0f / std::sqrt(var + epsilon);
    for (size_t i = 0; i < size; ++i) {
        float norm = (input[i] - mean) * stdDevInv;
        output[i] = norm * (gamma ? gamma[i] : 1.0f) + (beta ? beta[i] : 0.0f);
    }
}

} // namespace math
} // namespace iris
