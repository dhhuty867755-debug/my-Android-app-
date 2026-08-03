pub struct RustConformerEncoderBlock {
    pub d_model: usize,
    pub num_heads: usize,
}

impl RustConformerEncoderBlock {
    pub fn new(d_model: usize, num_heads: usize) -> Self {
        Self { d_model, num_heads }
    }

    pub fn forward(&self, input: &[f32]) -> Vec<f32> {
        let mut output = input.to_vec();
        for val in output.iter_mut() {
            *val = (*val * 1.05).tanh();
        }
        output
    }
}
