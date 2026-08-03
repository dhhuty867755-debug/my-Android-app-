#include <vector>
#include <cmath>
#include <algorithm>
#include <cstddef>
#include <stdexcept>
#include <android/log.h>

#define LOG_TAG "IrisTensorOps"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)

namespace iris {
namespace math {

class IrisTensorOperations {
public:
    static void applyGelu(float* data, size_t size) {
        const float sqrt_2_over_pi = 0.7978845608f;
        const float coef = 0.044715f;
        for (size_t i = 0; i < size; ++i) {
            float x = data[i];
            float cube = coef * x * x * x;
            float inner = sqrt_2_over_pi * (x + cube);
            data[i] = 0.5f * x * (1.0f + std::tanh(inner));
        }
    }

    static void applyRmsNorm(const float* input, float* output, const float* weight, size_t size, float eps = 1e-6f) {
        float sum_sq = 0.0f;
        for (size_t i = 0; i < size; ++i) {
            sum_sq += input[i] * input[i];
        }
        float mean_sq = sum_sq / static_cast<float>(size);
        float rms_inv = 1.0f / std::sqrt(mean_sq + eps);

        for (size_t i = 0; i < size; ++i) {
            output[i] = input[i] * rms_inv * (weight ? weight[i] : 1.0f);
        }
    }

    static void applyRotaryPositionalEmbedding(float* q, float* k, int seqLen, int headDim, int position) {
        for (int i = 0; i < headDim; i += 2) {
            float freq = 1.0f / std::pow(10000.0f, static_cast<float>(i) / static_cast<float>(headDim));
            float val = position * freq;
            float cos_val = std::cos(val);
            float sin_val = std::sin(val);

            float q0 = q[i];
            float q1 = q[i + 1];
            q[i] = q0 * cos_val - q1 * sin_val;
            q[i + 1] = q0 * sin_val + q1 * cos_val;

            if (k) {
                float k0 = k[i];
                float k1 = k[i + 1];
                k[i] = k0 * cos_val - k1 * sin_val;
                k[i + 1] = k0 * sin_val + k1 * cos_val;
            }
        }
    }

    static void computeAttentionScores(const float* Q, const float* K, float* attnOut, int seqLen, int headDim) {
        float scale = 1.0f / std::sqrt(static_cast<float>(headDim));
        for (int i = 0; i < seqLen; ++i) {
            for (int j = 0; j < seqLen; ++j) {
                float score = 0.0f;
                for (int d = 0; d < headDim; ++d) {
                    score += Q[i * headDim + d] * K[j * headDim + d];
                }
                attnOut[i * seqLen + j] = score * scale;
            }
        }
    }
};

} // namespace math
} // namespace iris
