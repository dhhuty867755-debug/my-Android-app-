#ifndef IRIS_ACOUSTIC_MODEL_HPP
#define IRIS_ACOUSTIC_MODEL_HPP

#include <vector>
#include <string>
#include <memory>
#include <future>

namespace iris {
namespace speech {

struct AcousticFeatureVector {
    std::vector<float> melSpectrogram;
    size_t timeSteps;
    size_t melBins;
};

struct HypothesizedToken {
    int tokenId;
    std::string text;
    float logProbability;
    float startTimestampSec;
    float endTimestampSec;
};

class IrisAcousticModel {
public:
    IrisAcousticModel(const std::string& modelPath, int numThreads = 4);
    ~IrisAcousticModel();

    bool loadModel(const std::string& modelPath);
    std::vector<HypothesizedToken> decodeBeamSearch(const AcousticFeatureVector& features, int beamWidth = 8);
    std::future<std::vector<HypothesizedToken>> decodeAsync(const AcousticFeatureVector& features);

private:
    std::string m_modelPath;
    int m_numThreads;
    bool m_isInitialized;

    void applyQuantizationScaling(float* data, size_t count, float scaleFactor);
};

} // namespace speech
} // namespace iris

#endif // IRIS_ACOUSTIC_MODEL_HPP
