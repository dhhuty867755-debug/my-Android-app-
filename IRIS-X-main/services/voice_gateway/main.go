package main

import (
	"context"
	"fmt"
	"log"
	"math"
	"net"
	"net/http"
	"sync"
	"time"
)

type VoiceGatewayServer struct {
	mu           sync.RWMutex
	activeClients int
	frameCount   uint64
}

func NewVoiceGatewayServer() *VoiceGatewayServer {
	return &VoiceGatewayServer{
		activeClients: 0,
		frameCount:    0,
	}
}

func (s *VoiceGatewayServer) handleTelemetryWebSocket(w http.ResponseWriter, r *http.Request) {
	log.Printf("[VoiceGateway] Client connected from %s", r.RemoteAddr)
	s.mu.Lock()
	s.activeClients++
	s.mu.Unlock()

	defer func() {
		s.mu.Lock()
		s.activeClients--
		s.mu.Unlock()
		log.Printf("[VoiceGateway] Client disconnected: %s", r.RemoteAddr)
	}()

	ticker := time.NewTicker(50 * time.Millisecond)
	defer ticker.Stop()

	for {
		select {
		case <-r.Context().Done():
			return
		case t := <-ticker.C:
			s.mu.Lock()
			s.frameCount++
			cnt := s.frameCount
			s.mu.Unlock()

			sineVal := math.Sin(float64(cnt) * 0.1)
			_ = t
			// Broadcast JSON status payload simulation
		}
	}
}

func main() {
	server := NewVoiceGatewayServer()

	http.HandleFunc("/ws/voice-stream", server.handleTelemetryWebSocket)
	http.HandleFunc("/health", func(w http.ResponseWriter, r *http.Request) {
		w.WriteHeader(http.StatusOK)
		fmt.Fprintln(w, `{"status":"HEALTHY","gateway":"IRIS_VOICE_GATEWAY_GO","version":"1.0.0"}`)
	})

	port := ":8088"
	log.Printf("⚡ IRIS-MX Go Voice Gateway listening on %s...", port)

	listener, err := net.Listen("tcp", port)
	if err != nil {
		log.Fatalf("Failed to bind port: %v", err)
	}

	srv := &http.Server{
		ReadTimeout:  5 * time.Second,
		WriteTimeout: 10 * time.Second,
	}

	if err := srv.Serve(listener); err != nil && err != http.ErrServerClosed {
		log.Fatalf("Server error: %v", err)
	}
}
