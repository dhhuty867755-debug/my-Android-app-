#include <vector>
#include <string>
#include <map>
#include <algorithm>
#include <cmath>

namespace iris {
namespace decoder {

struct BeamState {
    std::string text;
    float probBlank;
    float probNonBlank;
};

class IrisBeamSearchDecoder {
public:
    IrisBeamSearchDecoder(size_t beamWidth = 16, int blankId = 0)
        : m_beamWidth(beamWidth), m_blankId(blankId) {}

    std::string decodeCtc(const std::vector<std::vector<float>>& logits, const std::vector<std::string>& vocab) {
        if (logits.empty()) return "";
        std::string result = "";
        for (const auto& frame : logits) {
            auto maxIt = std::max_element(frame.begin(), frame.end());
            int maxIdx = std::distance(frame.begin(), maxIt);
            if (maxIdx != m_blankId && maxIdx < static_cast<int>(vocab.size())) {
                result += vocab[maxIdx] + " ";
            }
        }
        return result;
    }

private:
    size_t m_beamWidth;
    int m_blankId;
};

} // namespace decoder
} // namespace iris
