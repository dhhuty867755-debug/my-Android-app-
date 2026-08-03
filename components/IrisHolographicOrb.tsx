import React, { useEffect, useRef } from "react";
import { View, Text, Animated, StyleSheet, Easing } from "react-native";

export default function IrisHolographicOrb() {
  const pulseAnim = useRef(new Animated.Value(1)).current;
  const rotateAnim = useRef(new Animated.Value(0)).current;

  useEffect(() => {
    // Continuous Pulsing Animation
    Animated.loop(
      Animated.sequence([
        Animated.timing(pulseAnim, {
          toValue: 1.15,
          duration: 1800,
          easing: Easing.inOut(Easing.ease),
          useNativeDriver: true,
        }),
        Animated.timing(pulseAnim, {
          toValue: 1.0,
          duration: 1800,
          easing: Easing.inOut(Easing.ease),
          useNativeDriver: true,
        }),
      ])
    ).start();

    // Rotation Loop
    Animated.loop(
      Animated.timing(rotateAnim, {
        toValue: 1,
        duration: 8000,
        easing: Easing.linear,
        useNativeDriver: true,
      })
    ).start();
  }, [pulseAnim, rotateAnim]);

  const spin = rotateAnim.interpolate({
    inputRange: [0, 1],
    outputRange: ["0deg", "360deg"],
  });

  return (
    <View style={styles.container}>
      {/* Outer Holographic Glow Ring */}
      <Animated.View
        style={[
          styles.glowRing,
          {
            transform: [{ scale: pulseAnim }, { rotate: spin }],
          },
        ]}
      />

      {/* Inner Cyber Core Orb */}
      <Animated.View
        style={[
          styles.coreOrb,
          {
            transform: [{ scale: pulseAnim }],
          },
        ]}
      >
        <Text style={styles.coreText}>IRIS</Text>
        <Text style={styles.subText}>AI NODE</Text>
      </Animated.View>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    height: 180,
    width: "100%",
    justifyContent: "center",
    alignItems: "center",
    marginVertical: 10,
  },
  glowRing: {
    position: "absolute",
    width: 140,
    height: 140,
    borderRadius: 70,
    borderWidth: 2,
    borderColor: "#00F0FF",
    borderStyle: "dashed",
    backgroundColor: "rgba(0, 240, 255, 0.06)",
  },
  coreOrb: {
    width: 100,
    height: 100,
    borderRadius: 50,
    backgroundColor: "#7000FF",
    justifyContent: "center",
    alignItems: "center",
    borderWidth: 2,
    borderColor: "#FF007A",
    shadowColor: "#00F0FF",
    shadowOffset: { width: 0, height: 0 },
    shadowOpacity: 0.9,
    shadowRadius: 16,
    elevation: 10,
  },
  coreText: {
    color: "#FFFFFF",
    fontSize: 18,
    fontWeight: "bold",
    letterSpacing: 2,
    fontFamily: "monospace",
  },
  subText: {
    color: "#00F0FF",
    fontSize: 9,
    fontFamily: "monospace",
    letterSpacing: 1,
  },
});
