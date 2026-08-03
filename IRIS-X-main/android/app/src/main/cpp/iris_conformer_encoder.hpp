#ifndef IRIS_CONFORMER_ENCODER_HPP
#define IRIS_CONFORMER_ENCODER_HPP

#include <vector>
#include <memory>

namespace iris {
namespace speech {

class IrisConformerEncoder {
public:
    IrisConformerEncoder(int dModel = 256, int numHeads = 4, int kernelSize = 31);
    ~IrisConformerEncoder();

    std::vector<float> forward(const std::vector<float>& inputMelFrames, int seqLen);
    void applyFeedForwardLayer(float* data, size_t size);

private:
    int m_dModel;
    int m_numHeads;
    int m_kernelSize;
};

} // namespace speech
} // namespace iris

#endif // IRIS_CONFORMER_ENCODER_HPP
