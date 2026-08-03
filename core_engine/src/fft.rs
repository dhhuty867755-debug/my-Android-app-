use std::f32::consts::PI;

pub struct FastFourierTransform;

impl FastFourierTransform {
    pub fn compute_fft(real: &mut [f32], imag: &mut [f32]) {
        let n = real.len();
        if n <= 1 { return; }

        let mut j = 0;
        for i in 0..n {
            if j > i {
                real.swap(i, j);
                imag.swap(i, j);
            }
            let mut m = n >> 1;
            while m >= 1 && j >= m {
                j -= m;
                m >>= 1;
            }
            j += m;
        }

        let mut len = 2;
        while len <= n {
            let ang = 2.0 * PI / len as f32;
            let wlen_r = ang.cos();
            let wlen_i = -ang.sin();

            for i in (0..n).step_by(len) {
                let mut w_r = 1.0f32;
                let mut w_i = 0.0f32;

                for k in 0..(len / 2) {
                    let u_r = real[i + k];
                    let u_i = imag[i + k];
                    let v_r = real[i + k + len / 2] * w_r - imag[i + k + len / 2] * w_i;
                    let v_i = real[i + k + len / 2] * w_i + imag[i + k + len / 2] * w_r;

                    real[i + k] = u_r + v_r;
                    imag[i + k] = u_i + v_i;
                    real[i + k + len / 2] = u_r - v_r;
                    imag[i + k + len / 2] = u_i - v_i;

                    let next_w_r = w_r * wlen_r - w_i * wlen_i;
                    let next_w_i = w_r * wlen_i + w_i * wlen_r;
                    w_r = next_w_r;
                    w_i = next_w_i;
                }
            }
            len <<= 1;
        }
    }
}
