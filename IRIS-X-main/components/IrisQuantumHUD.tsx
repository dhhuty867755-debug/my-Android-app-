import React from "react";
import { View, Text, StyleSheet } from "react-native";

export default function IrisQuantumHUD() {
  return (
    <View style={styles.hudCard}>
      <Text style={styles.hudTitle}>🌐 IRIS MULTI-SERVICE SYSTEM MATRIX</Text>
      
      <View style={styles.gridContainer}>
        <View style={styles.gridItem}>
          <Text style={styles.gridLabel}>RUST VAD DSP</Text>
          <Text style={styles.gridValue}>ACTIVE (0.1ms)</Text>
        </View>

        <View style={styles.gridItem}>
          <Text style={styles.gridLabel}>GO GATEWAY</Text>
          <Text style={styles.gridValue}>WS :8088 READY</Text>
        </View>

        <View style={styles.gridItem}>
          <Text style={styles.gridLabel}>C++ NDK BEAM ASR</Text>
          <Text style={styles.gridValue}>LATENCY 1.3ms</Text>
        </View>

        <View style={styles.gridItem}>
          <Text style={styles.gridLabel}>PROTOTOBUF ENCODER</Text>
          <Text style={styles.gridValue}>PCM 44.1kHz</Text>
        </View>
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  hudCard: {
    width: "100%",
    padding: 14,
    borderRadius: 16,
    backgroundColor: "#0A0314",
    borderWidth: 1,
    borderColor: "rgba(112, 0, 255, 0.4)",
    marginVertical: 8,
  },
  hudTitle: {
    color: "#FF007A",
    fontSize: 12,
    fontWeight: "bold",
    fontFamily: "monospace",
    letterSpacing: 1,
    marginBottom: 10,
  },
  gridContainer: {
    flexDirection: "row",
    flexWrap: "wrap",
    justifyContent: "space-between",
    gap: 8,
  },
  gridItem: {
    width: "48%",
    padding: 10,
    backgroundColor: "rgba(255,255,255,0.03)",
    borderRadius: 8,
    borderLeftWidth: 3,
    borderLeftColor: "#00F0FF",
  },
  gridLabel: {
    color: "#A0AEC0",
    fontSize: 9,
    fontFamily: "monospace",
  },
  gridValue: {
    color: "#FFFFFF",
    fontSize: 11,
    fontWeight: "bold",
    fontFamily: "monospace",
    marginTop: 2,
  },
});
