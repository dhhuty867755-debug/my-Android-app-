#[no_mangle]
pub extern "C" fn rust_ffi_vad_detect(samples: *const f32, len: usize) -> f32 {
    if samples.is_null() || len == 0 {
        return 0.0;
    }
    let slice = unsafe { std::slice::from_raw_parts(samples, len) };
    let sum_sq: f32 = slice.iter().map(|&s| s * s).sum();
    (sum_sq / len as f32).sqrt()
}

#[no_mangle]
pub extern "C" fn rust_ffi_version() -> i32 {
    240
}
