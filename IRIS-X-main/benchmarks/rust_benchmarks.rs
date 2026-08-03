use criterion::{black_box, criterion_group, criterion_main, Criterion};

// Microsecond Rust VAD Benchmark
pub fn benchmark_vad_throughput(c: &mut Criterion) {
    let dummy_samples = vec![0.05f32; 512];
    c.bench_function("rust_vad_process_frame_512", |b| {
        b.iter(|| {
            let sum_sq: f32 = dummy_samples.iter().map(|&s| s * s).sum();
            let rms = (sum_sq / dummy_samples.len() as f32).sqrt();
            black_box(rms);
        })
    });
}

criterion_group!(benches, benchmark_vad_throughput);
criterion_main!(benches);
