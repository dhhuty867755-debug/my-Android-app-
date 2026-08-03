use std::collections::HashMap;

#[derive(Debug, Clone)]
pub struct PrefixBeamCandidate {
    pub prefix: Vec<usize>,
    pub p_blank: f32,
    pub p_non_blank: f32,
}

pub struct CtcPrefixBeamSearchDecoder {
    pub beam_width: usize,
    pub blank_id: usize,
    pub vocabulary: Vec<String>,
}

impl CtcPrefixBeamSearchDecoder {
    pub fn new(beam_width: usize, blank_id: usize, vocabulary: Vec<String>) -> Self {
        Self {
            beam_width,
            blank_id,
            vocabulary,
        }
    }

    pub fn decode(&self, ctc_probs: &[Vec<f32>]) -> String {
        let mut beams: HashMap<Vec<usize>, PrefixBeamCandidate> = HashMap::new();
        beams.insert(
            vec![],
            PrefixBeamCandidate {
                prefix: vec![],
                p_blank: 1.0,
                p_non_blank: 0.0,
            },
        );

        for step in ctc_probs {
            let mut next_beams: HashMap<Vec<usize>, PrefixBeamCandidate> = HashMap::new();

            for (prefix, candidate) in beams.iter() {
                let p_total = candidate.p_blank + candidate.p_non_blank;

                // 1. Process Blank Transition
                let p_b = step[self.blank_id];
                let entry = next_beams.entry(prefix.clone()).or_insert(PrefixBeamCandidate {
                    prefix: prefix.clone(),
                    p_blank: 0.0,
                    p_non_blank: 0.0,
                });
                entry.p_blank += p_total * p_b;

                // 2. Process Non-Blank Transitions
                for (token_id, &p_token) in step.iter().enumerate() {
                    if token_id == self.blank_id {
                        continue;
                    }

                    let mut new_prefix = prefix.clone();
                    new_prefix.push(token_id);

                    let entry = next_beams.entry(new_prefix.clone()).or_insert(PrefixBeamCandidate {
                        prefix: new_prefix,
                        p_blank: 0.0,
                        p_non_blank: 0.0,
                    });
                    entry.p_non_blank += p_total * p_token;
                }
            }

            // Prune beams to beam_width
            let mut sorted_beams: Vec<_> = next_beams.into_iter().collect();
            sorted_beams.sort_by(|a, b| {
                let score_a = a.1.p_blank + a.1.p_non_blank;
                let score_b = b.1.p_blank + b.1.p_non_blank;
                score_b.partial_cmp(&score_a).unwrap_or(std::cmp::Ordering::Equal)
            });

            beams = sorted_beams.into_iter().take(self.beam_width).collect();
        }

        // Return top prefix string
        if let Some((best_prefix, _)) = beams.into_iter().next() {
            best_prefix
                .iter()
                .filter_map(|&id| self.vocabulary.get(id))
                .cloned()
                .collect::<Vec<_>>()
                .join("")
        } else {
            String::new()
        }
    }
}
