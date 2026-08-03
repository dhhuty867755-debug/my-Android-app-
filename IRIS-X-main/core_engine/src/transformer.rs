pub struct RustTransformerBlock {
    pub d_model: usize,
    pub n_heads: usize,
    pub d_head: usize,
}

impl RustTransformerBlock {
    pub fn new(d_model: usize, n_heads: usize) -> Self {
        let d_head = d_model / n_heads;
        Self { d_model, n_heads, d_head }
    }

    pub fn forward_attention(&self, query: &[f32], key: &[f32], value: &[f32]) -> Vec<f32> {
        let mut output = vec![0.0f32; self.d_model];
        let scale = 1.0 / (self.d_head as f32).sqrt();

        for i in 0..self.d_model {
            output[i] = (query[i] * key[i] * scale).tanh() * value[i];
        }
        output
    }
}
