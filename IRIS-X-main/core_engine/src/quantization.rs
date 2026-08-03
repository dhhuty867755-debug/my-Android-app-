pub struct QuantizedTensor {
    pub data: Vec<i8>,
    pub scale: f32,
    pub zero_point: i8,
    pub shape: Vec<usize>,
}

pub struct TensorQuantizer;

impl TensorQuantizer {
    pub fn quantize_int8_symmetric(weights: &[f32], shape: Vec<usize>) -> QuantizedTensor {
        let mut max_abs = 0.0f32;
        for &w in weights {
            if w.abs() > max_abs {
                max_abs = w.abs();
            }
        }

        let scale = if max_abs > 0.0 { max_abs / 127.0 } else { 1.0 };
        let mut data = Vec::with_capacity(weights.len());

        for &w in weights {
            let q = (w / scale).round().clamp(-127.0, 127.0) as i8;
            data.push(q);
        }

        QuantizedTensor {
            data,
            scale,
            zero_point: 0,
            shape,
        }
    }

    pub fn dequantize_int8_symmetric(quantized: &QuantizedTensor) -> Vec<f32> {
        let mut output = Vec::with_capacity(quantized.data.len());
        for &q in &quantized.data {
            output.push(q as f32 * quantized.scale);
        }
        output
    }
}
