package main

import (
	"encoding/json"
	"fmt"
	"log"
	"net/http"
	"sync"
	"time"
)

type ModelArtifact struct {
	ModelID     string    `json:"model_id"`
	Version     string    `json:"version"`
	Format      string    `json:"format"`
	SizeBytes   int64     `json:"size_bytes"`
	UploadedAt  time.Time `json:"uploaded_at"`
	DownloadURL string    `json:"download_url"`
}

type ModelRegistry struct {
	mu     sync.RWMutex
	models map[string]*ModelArtifact
}

func NewModelRegistry() *ModelRegistry {
	r := &ModelRegistry{
		models: make(map[string]*ModelArtifact),
	}
	r.models["conformer-asr-v1"] = &ModelArtifact{
		ModelID:     "conformer-asr-v1",
		Version:     "1.0.0",
		Format:      "ONNX",
		SizeBytes:   65685,
		UploadedAt:  time.Now(),
		DownloadURL: "https://models.irisxai.in/onnx/iris_conformer_asr_v1.onnx",
	}
	r.models["llama-3-8b-int4"] = &ModelArtifact{
		ModelID:     "llama-3-8b-int4",
		Version:     "3.0.0",
		Format:      "GGUF",
		SizeBytes:   91312,
		UploadedAt:  time.Now(),
		DownloadURL: "https://models.irisxai.in/gguf/iris_llama_3_int4.gguf",
	}
	return r
}

func main() {
	registry := NewModelRegistry()

	http.HandleFunc("/api/v1/models/list", func(w http.ResponseWriter, r *http.Request) {
		registry.mu.RLock()
		defer registry.mu.RUnlock()
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(registry.models)
	})

	port := ":9094"
	log.Printf("⚡ IRIS-MX Go Model Registry listening on %s...", port)
	if err := http.ListenAndServe(port, nil); err != nil {
		log.Fatalf("Server error: %v", err)
	}
}
