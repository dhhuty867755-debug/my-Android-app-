{
  "targets": [
    {
      "target_name": "iris_node_addon",
      "sources": [
        "iris_node_addon.cpp",
        "../../android/app/src/main/cpp/iris_core_engine.cpp",
        "../../android/app/src/main/cpp/iris_simd_matrix.cpp"
      ],
      "include_dirs": [
        "../../android/app/src/main/cpp"
      ],
      "cflags!": [ "-fno-exceptions" ],
      "cflags_cc!": [ "-fno-exceptions" ]
    }
  ]
}
