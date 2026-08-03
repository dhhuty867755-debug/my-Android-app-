cmake_minimum_required(VERSION 3.22.1)
project("iris_native_engine")

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

add_library(
    iris_native_engine
    SHARED
    iris_core_engine.cpp
    iris_jni_bridge.cpp
    iris_simd_matrix.cpp
    iris_vulkan_compute.cpp
    iris_audio_resampler.cpp
    iris_tensor_math.cpp
    iris_conformer_encoder.cpp
    iris_beam_search_decoder.cpp
    iris_quantized_matmul.cpp
    iris_tensor_operations.cpp
    iris_audio_dsp_kernel.cpp
    raw_c_dsp.c
    iris_fft_radix4.c
    iris_filterbank.c
)

find_library(
    log-lib
    log
)

target_link_libraries(
    iris_native_engine
    ${log-lib}
)
