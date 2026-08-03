package main

import (
	"crypto/hmac"
	"crypto/sha256"
	"encoding/base64"
	"encoding/json"
	"fmt"
	"log"
	"net/http"
	"strings"
	"time"
)

type TokenClaims struct {
	UserID    string `json:"sub"`
	Role      string `json:"role"`
	IssuedAt  int64  `json:"iat"`
	ExpiresAt int64  `json:"exp"`
}

type AuthServer struct {
	secretKey []byte
}

func NewAuthServer(secret string) *AuthServer {
	return &AuthServer{
		secretKey: []byte(secret),
	}
}

func (a *AuthServer) GenerateToken(userID, role string) (string, error) {
	header := base64.RawURLEncoding.EncodeToString([]byte(`{"alg":"HS256","typ":"JWT"}`))
	claims := TokenClaims{
		UserID:    userID,
		Role:      role,
		IssuedAt:  time.Now().Unix(),
		ExpiresAt: time.Now().Add(24 * time.Hour).Unix(),
	}
	claimsBytes, _ := json.Marshal(claims)
	payload := base64.RawURLEncoding.EncodeToString(claimsBytes)

	unsignedToken := fmt.Sprintf("%s.%s", header, payload)
	h := hmac.New(sha256.New, a.secretKey)
	h.Write([]byte(unsignedToken))
	sig := base64.RawURLEncoding.EncodeToString(h.Sum(nil))

	return fmt.Sprintf("%s.%s", unsignedToken, sig), nil
}

func main() {
	server := NewAuthServer("IRIS_MASTER_SECRET_KEY_2026")

	http.HandleFunc("/api/v1/auth/token", func(w http.ResponseWriter, r *http.Request) {
		token, _ := server.GenerateToken("user_201harsh", "ADMIN")
		w.Header().Set("Content-Type", "application/json")
		fmt.Fprintf(w, `{"access_token":"%s","token_type":"Bearer","expires_in":86400}`, token)
	})

	port := ":9093"
	log.Printf("⚡ IRIS-MX Go OAuth2 Auth Server listening on %s...", port)
	if err := http.ListenAndServe(port, nil); err != nil {
		log.Fatalf("Server error: %v", err)
	}
}
