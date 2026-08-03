package main

import (
	"context"
	"encoding/json"
	"fmt"
	"log"
	"net/http"
	"strings"
	"sync"
	"time"
)

type IntentRequest struct {
	QueryText  string            `json:"query_text"`
	AudioRms   float64           `json:"audio_rms"`
	SessionID  string            `json:"session_id"`
	Context    map[string]string `json:"context"`
}

type IntentResponse struct {
	IntentName     string            `json:"intent_name"`
	Confidence     float64           `json:"confidence"`
	Slots          map[string]string `json:"slots"`
	ExecutionTarget string           `json:"execution_target"`
	LatencyMs      int64             `json:"latency_ms"`
}

type IntentRouterService struct {
	mu           sync.Mutex
	routedCount  uint64
}

func NewIntentRouterService() *IntentRouterService {
	return &IntentRouterService{}
}

func (s *IntentRouterService) HandleRouteIntent(w http.ResponseWriter, r *http.Request) {
	startTime := time.Now()
	if r.Method != http.MethodPost {
		http.Error(w, "Method not allowed", http.StatusMethodNotAllowed)
		return
	}

	var req IntentRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		http.Error(w, err.Error(), http.StatusBadRequest)
		return
	}

	s.mu.Lock()
	s.routedCount++
	s.mu.Unlock()

	queryLower := strings.ToLower(req.QueryText)
	intent := "UNKNOWN_COMMAND"
	confidence := 0.94
	slots := make(map[string]string)
	target := "KOTLIN_DSP_ENGINE"

	if strings.Contains(queryLower, "status") || strings.Contains(queryLower, "telemetry") {
		intent = "QUERY_SYSTEM_TELEMETRY"
		target = "C_CPP_NDK_ENGINE"
		slots["category"] = "HARDWARE_METRICS"
	} else if strings.Contains(queryLower, "voice") || strings.Contains(queryLower, "auth") {
		intent = "TRIGGER_BIOMETRIC_AUTH"
		target = "JAVA_SECURITY_AUTHENTICATOR"
		slots["security_level"] = "ZERO_TRUST_PASSIVE"
	} else if strings.Contains(queryLower, "visualize") || strings.Contains(queryLower, "spectrum") {
		intent = "ACTIVATE_SPECTRUM_ORB"
		target = "KOTLIN_CANVAS_VIEW"
		slots["mode"] = "QUANTUM_PARTICLE_MATRIX"
	}

	resp := IntentResponse{
		IntentName:      intent,
		Confidence:      confidence,
		Slots:           slots,
		ExecutionTarget: target,
		LatencyMs:       time.Since(startTime).Milliseconds(),
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(resp)
}

func main() {
	service := NewIntentRouterService()
	http.HandleFunc("/api/v1/intent/route", service.HandleRouteIntent)

	port := ":8089"
	log.Printf("⚡ IRIS-MX Go Intent Router Service listening on %s...", port)
	if err := http.ListenAndServe(port, nil); err != nil {
		log.Fatalf("Server error: %v", err)
	}
}
