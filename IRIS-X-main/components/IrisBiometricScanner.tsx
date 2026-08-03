import React, { useState, useEffect } from "react";
import { View, Text, StyleSheet, TouchableOpacity } from "react-native";

export default function IrisBiometricScanner() {
  const [isScanning, setIsScanning] = useState(false);
  const [authenticated, setAuthenticated] = useState(true);
  const [confidence, setConfidence] = useState(0.96);

  const handleScan = () => {
    setIsScanning(true);
    setTimeout(() => {
      setIsScanning(false);
      setAuthenticated(true);
      setConfidence(0.94 + Math.random() * 0.05);
    }, 1200);
  };

  return (
    <View style={styles.card}>
      <View style={styles.headerRow}>
        <Text style={styles.title}>🔒 BIOMETRIC VOICEPRINT AUTH</Text>
        <View
          style={[
            styles.badge,
            { backgroundColor: authenticated ? "rgba(0, 240, 255, 0.2)" : "rgba(255, 0, 122, 0.2)" },
          ]}
        >
          <Text
            style={[
              styles.badgeText,
              { color: authenticated ? "#00F0FF" : "#FF007A" },
            ]}
          >
            {isScanning ? "SCANNING..." : authenticated ? "VERIFIED" : "LOCKED"}
          </Text>
        </View>
      </View>

      <Text style={styles.metricsText}>
        SPEAKER ID : HARSH_VOICEPRINT_v2 (SHA-256)
      </Text>
      <Text style={styles.metricsText}>
        COSINE MATCH: {(confidence * 100).toFixed(1)}% (THRESHOLD: &gt; 88%)
      </Text>

      {/* Simulated Waveform Analyzer Bars */}
      <View style={styles.waveRow}>
        {[0.4, 0.8, 0.3, 0.95, 0.6, 1.0, 0.7, 0.45, 0.85, 0.2, 0.9, 0.5].map((val, idx) => (
          <View
            key={idx}
            style={[
              styles.waveBar,
              {
                height: isScanning ? Math.random() * 28 + 6 : val * 24 + 4,
                backgroundColor: idx % 2 === 0 ? "#00F0FF" : "#FF007A",
              },
            ]}
          />
        ))}
      </View>

      <TouchableOpacity
        style={[styles.button, isScanning && styles.buttonActive]}
        onPress={handleScan}
        disabled={isScanning}
      >
        <Text style={styles.buttonText}>
          {isScanning ? "VERIFYING VOICE VECTOR..." : "RE-AUTHENTICATE VOICE PRINT"}
        </Text>
      </TouchableOpacity>
    </View>
  );
}

const styles = StyleSheet.create({
  card: {
    width: "100%",
    padding: 16,
    borderRadius: 16,
    backgroundColor: "#0F061E",
    borderWidth: 1,
    borderColor: "rgba(0, 240, 255, 0.25)",
    marginVertical: 8,
  },
  headerRow: {
    flexDirection: "row",
    justifyContent: "space-between",
    alignItems: "center",
    marginBottom: 10,
  },
  title: {
    color: "#00F0FF",
    fontSize: 13,
    fontWeight: "bold",
    fontFamily: "monospace",
  },
  badge: {
    paddingHorizontal: 8,
    paddingVertical: 4,
    borderRadius: 6,
  },
  badgeText: {
    fontSize: 10,
    fontWeight: "bold",
    fontFamily: "monospace",
  },
  metricsText: {
    color: "#A0AEC0",
    fontSize: 11,
    fontFamily: "monospace",
    marginVertical: 2,
  },
  waveRow: {
    flexDirection: "row",
    alignItems: "center",
    justifyContent: "space-between",
    height: 36,
    marginVertical: 10,
    paddingHorizontal: 8,
    backgroundColor: "rgba(0,0,0,0.4)",
    borderRadius: 8,
  },
  waveBar: {
    width: 4,
    borderRadius: 2,
  },
  button: {
    marginTop: 6,
    paddingVertical: 10,
    borderRadius: 8,
    backgroundColor: "#7000FF",
    alignItems: "center",
  },
  buttonActive: {
    opacity: 0.6,
  },
  buttonText: {
    color: "#FFFFFF",
    fontSize: 11,
    fontWeight: "bold",
    fontFamily: "monospace",
    letterSpacing: 0.5,
  },
});
