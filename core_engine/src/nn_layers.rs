pub struct DenseLayer {
    pub weights: Vec<f32>,
    pub bias: Vec<f32>,
    pub in_features: usize,
    pub out_features: usize,
}

impl DenseLayer {
    pub fn new(in_features: usize, out_features: usize) -> Self {
        Self {
            weights: vec![0.01; in_features * out_features],
            bias: vec![0.0; out_features],
            in_features,
            out_features,
        }
    }

    pub fn forward(&self, input: &[f32]) -> Vec<f32> {
        let mut output = vec![0.0f32; self.out_features];
        for o in 0..self.out_features {
            let mut sum = self.bias[o];
            for i in 0..self.in_features {
                sum += input[i] * self.weights[o * self.in_features + i];
            }
            output[o] = sum.tanh();
        }
        output
    }
}
