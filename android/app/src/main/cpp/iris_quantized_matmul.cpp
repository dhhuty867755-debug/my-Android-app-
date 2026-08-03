#include <vector>
#include <cstdint>
#include <cmath>

namespace iris {
namespace quant {

void matmulInt8(const int8_t* A, const int8_t* B, float* C, int M, int N, int K, float scaleA, float scaleB) {
    float combinedScale = scaleA * scaleB;
    for (int m = 0; m < M; ++m) {
        for (int n = 0; n < N; ++n) {
            int32_t sum = 0;
            for (int k = 0; k < K; ++k) {
                sum += static_cast<int32_t>(A[m * K + k]) * static_cast<int32_t>(B[k * N + n]);
            }
            C[m * N + n] = static_cast<float>(sum) * combinedScale;
        }
    }
}

} // namespace quant
} // namespace iris
