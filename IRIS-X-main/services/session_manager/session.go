package main

import (
	"crypto/rand"
	"encoding/hex"
	"fmt"
	"log"
	"net/http"
	"sync"
	"time"
)

type VoiceSession struct {
	SessionID     string    `json:"session_id"`
	UserID        string    `json:"user_id"`
	CreatedAt     time.Time `json:"created_at"`
	LastActive    time.Time `json:"last_active"`
	AudioFormat   string    `json:"audio_format"`
	SampleRate    int       `json:"sample_rate"`
	IsAuthVerified bool     `json:"is_auth_verified"`
}

type SessionManager struct {
	mu       sync.RWMutex
	sessions map[string]*VoiceSession
}

func NewSessionManager() *SessionManager {
	return &SessionManager{
		sessions: make(map[string]*VoiceSession),
	}
}

func (sm *SessionManager) CreateSession(userID string) *VoiceSession {
	sm.mu.Lock()
	defer sm.mu.Unlock()

	bytes := make([]byte, 16)
	rand.Read(bytes)
	sessionID := "sess_" + hex.EncodeToString(bytes)

	sess := &VoiceSession{
		SessionID:      sessionID,
		UserID:         userID,
		CreatedAt:      time.Now(),
		LastActive:     time.Now(),
		AudioFormat:    "PCM_16BIT_MONO",
		SampleRate:     44100,
		IsAuthVerified: true,
	}

	sm.sessions[sessionID] = sess
	log.Printf("[SessionManager] Created new voice session: %s for user %s", sessionID, userID)
	return sess
}

func main() {
	sm := NewSessionManager()

	http.HandleFunc("/api/v1/session/create", func(w http.ResponseWriter, r *http.Request) {
		sess := sm.CreateSession("user_201harsh")
		w.Header().Set("Content-Type", "application/json")
		fmt.Fprintf(w, `{"session_id":"%s","status":"ACTIVE","sample_rate":44100}`, sess.SessionID)
	})

	port := ":9091"
	log.Printf("⚡ IRIS-MX Go Session Manager listening on %s...", port)
	if err := http.ListenAndServe(port, nil); err != nil {
		log.Fatalf("Server error: %v", err)
	}
}
