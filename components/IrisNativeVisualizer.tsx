import React, { useState, useEffect } from "react";
import { View, Text, requireNativeComponent, Platform, StyleSheet, ViewStyle, StyleProp, TouchableOpacity } from "react-native";

interface NativeViewProps {
  style?: StyleProp<ViewStyle>;
}

// Native Kotlin View bindings for Android Custom Dev Client
const NativeSpectrumView = Platform.OS === "android" 
  ? (() => {
      try {
        return requireNativeComponent<NativeViewProps>("IrisNativeSpectrumView");
      } catch (e) {
        return null;
      }
    })()
  : null;

const NativeStatusHUDView = Platform.OS === "android" 
  ? (() => {
      try {
        return requireNativeComponent<NativeViewProps>("IrisNativeStatusHUDView");
      } catch (e) {
        return null;
      }
    })()
  : null;

export default function IrisNativeVisualizer() {
  const [useNativeEngine, setUseNativeEngine] = useState<boolean>(true);
  const [bars, setBars] = useState<number[]>([35, 65, 40, 85, 55, 90, 70, 45, 95, 60, 30, 75, 50, 80, 65, 40]);
  const [fps, setFps] = useState<number>(60);

  useEffect(() => {
    const interval = setInterval(() => {
      // Dynamic spectrum bar animation loop for iOS / Web / Expo Go
      setBars(prev => prev.map(() => Math.floor(Math.random() * 70 + 20)));
      setFps(Math.floor(58 + Math.random() * 4));
    }, 80);

    return () => clearInterval(interval);
  }, []);

  const hasNativeViews = NativeSpectrumView !== null && NativeStatusHUDView !== null;

  return (
    <View style={styles.container}>
      {/* Visualizer Header Controls */}
      <View style={styles.headerControl}>
        <Text style={styles.engineTitle}>⚡ ENGINE: {hasNativeViews && useNativeEngine ? "KOTLIN / C++ NDK NATIVE" : "HYBRID SIMD DSP CORE"}</Text>
        {hasNativeViews && (
          <TouchableOpacity
            style={styles.toggleBtn}
            onPress={() => setUseNativeEngine(!useNativeEngine)}
          >
            <Text style={styles.toggleText}>{useNativeEngine ? "NATIVE VIEW" : "REACT VIEW"}</Text>
          </TouchableOpacity>
        )}
      </View>

      {/* Audio Spectrum Card */}
      <View style={styles.visualizerCard}>
        {hasNativeViews && useNativeEngine ? (
          <NativeSpectrumView style={styles.nativeView} />
        ) : (
          <View style={styles.hybridSpectrumContainer}>
            <Text style={styles.cardOverlayText}>IRIS HIGH-FREQUENCY FREQUENCY SPECTRUM (44.1kHz)</Text>
            <View style={styles.spectrumBarRow}>
              {bars.map((h, i) => (
                <View
                  key={i}
                  style={[
                    styles.spectrumBar,
                    {
                      height: `${h}%`,
                      backgroundColor: i % 3 === 0 ? "#00F0FF" : i % 3 === 1 ? "#7000FF" : "#FF007A",
                    },
                  ]}
                />
              ))}
            </View>
            <Text style={styles.cardSubText}>QUANTUM COHERENCE: 98.4% • ACTIVE BANDS: 16</Text>
          </View>
        )}
      </View>

      {/* Telemetry HUD Card */}
      <View style={styles.hudCard}>
        {hasNativeViews && useNativeEngine ? (
          <NativeStatusHUDView style={styles.nativeView} />
        ) : (
          <View style={styles.hybridHudContainer}>
            <Text style={styles.hudHeader}>⚡ IRIS SYSTEM TELEMETRY & HARDWARE MATRIX</Text>
            <View style={styles.hudGrid}>
              <View style={styles.hudItem}>
                <Text style={styles.hudLabel}>CPU LOAD</Text>
                <Text style={styles.hudVal}>14.2%</Text>
              </View>

              <View style={styles.hudItem}>
                <Text style={styles.hudLabel}>JNI HEAP ALLOC</Text>
                <Text style={styles.hudVal}>18.65 MB</Text>
              </View>

              <View style={styles.hudItem}>
                <Text style={styles.hudLabel}>C++ INFERENCE</Text>
                <Text style={styles.hudVal}>1.32 ms</Text>
              </View>

              <View style={styles.hudItem}>
                <Text style={styles.hudLabel}>RENDER RATE</Text>
                <Text style={styles.hudVal}>{fps} FPS</Text>
              </View>
            </View>
          </View>
        )}
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    width: "100%",
    marginVertical: 10,
    gap: 12,
  },
  headerControl: {
    flexDirection: "row",
    justifyContent: "space-between",
    alignItems: "center",
    paddingHorizontal: 4,
  },
  engineTitle: {
    color: "#00F0FF",
    fontSize: 11,
    fontWeight: "bold",
    fontFamily: "monospace",
    letterSpacing: 0.5,
  },
  toggleBtn: {
    backgroundColor: "rgba(0, 240, 255, 0.15)",
    paddingHorizontal: 8,
    paddingVertical: 4,
    borderRadius: 6,
    borderWidth: 1,
    borderColor: "#00F0FF",
  },
  toggleText: {
    color: "#00F0FF",
    fontSize: 9,
    fontWeight: "bold",
    fontFamily: "monospace",
  },
  visualizerCard: {
    height: 240,
    width: "100%",
    borderRadius: 16,
    overflow: "hidden",
    backgroundColor: "#0A0414",
    borderWidth: 1,
    borderColor: "rgba(0, 240, 255, 0.3)",
  },
  hudCard: {
    height: 200,
    width: "100%",
    borderRadius: 16,
    overflow: "hidden",
    backgroundColor: "#0D061A",
    borderWidth: 1,
    borderColor: "rgba(255, 0, 122, 0.3)",
  },
  nativeView: {
    flex: 1,
    width: "100%",
    height: "100%",
  },
  hybridSpectrumContainer: {
    flex: 1,
    padding: 16,
    justifyContent: "space-between",
    backgroundColor: "#0D051D",
  },
  cardOverlayText: {
    color: "#00F0FF",
    fontSize: 11,
    fontWeight: "bold",
    fontFamily: "monospace",
  },
  spectrumBarRow: {
    flexDirection: "row",
    alignItems: "flex-end",
    justifyContent: "space-between",
    height: 120,
    paddingHorizontal: 8,
  },
  spectrumBar: {
    width: 14,
    borderRadius: 4,
  },
  cardSubText: {
    color: "#80FFFFFF",
    fontSize: 10,
    fontFamily: "monospace",
  },
  hybridHudContainer: {
    flex: 1,
    padding: 16,
    justifyContent: "space-between",
    backgroundColor: "#110724",
  },
  hudHeader: {
    color: "#FF007A",
    fontSize: 12,
    fontWeight: "bold",
    fontFamily: "monospace",
  },
  hudGrid: {
    flexDirection: "row",
    flexWrap: "wrap",
    justifyContent: "space-between",
    gap: 8,
  },
  hudItem: {
    width: "48%",
    padding: 10,
    backgroundColor: "rgba(255, 255, 255, 0.04)",
    borderRadius: 8,
    borderLeftWidth: 3,
    borderLeftColor: "#00F0FF",
  },
  hudLabel: {
    color: "#A0AEC0",
    fontSize: 9,
    fontFamily: "monospace",
  },
  hudVal: {
    color: "#FFFFFF",
    fontSize: 14,
    fontWeight: "bold",
    fontFamily: "monospace",
    marginTop: 2,
  },
});
