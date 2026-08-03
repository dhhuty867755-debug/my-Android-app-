use std::collections::HashMap;

pub struct RustBpeTokenizer {
    pub vocab: HashMap<String, usize>,
    pub inv_vocab: HashMap<usize, String>,
}

impl RustBpeTokenizer {
    pub fn new() -> Self {
        let mut vocab = HashMap::new();
        let mut inv_vocab = HashMap::new();
        let tokens = vec!["[PAD]", "[UNK]", "IRIS", "AI", "SPEECH", "ENGINE", "ACTIVE"];
        for (i, &t) in tokens.iter().enumerate() {
            vocab.insert(t.to_string(), i);
            inv_vocab.insert(i, t.to_string());
        }
        Self { vocab, inv_vocab }
    }

    pub fn encode(&self, text: &str) -> Vec<usize> {
        text.split_whitespace()
            .map(|word| *self.vocab.get(word).unwrap_or(&1))
            .collect()
    }

    pub fn decode(&self, tokens: &[usize]) -> String {
        tokens
            .iter()
            .map(|&id| self.inv_vocab.get(&id).cloned().unwrap_or("[UNK]".to_string()))
            .collect::<Vec<_>>()
            .join(" ")
    }
}
