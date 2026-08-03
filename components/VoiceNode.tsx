import { Mic } from "lucide-react-native";
import React, { useEffect, useRef, useState } from "react";
import { Animated, Pressable, Text, View } from "react-native";

const AnimatedPressable = Animated.createAnimatedComponent(Pressable);

const VoiceNode = () => {
  const [listening, setListening] = useState(false);

  const pulseAnim = useRef(new Animated.Value(0)).current;

  useEffect(() => {
    if (listening) {
      Animated.loop(
        Animated.sequence([
          Animated.timing(pulseAnim, {
            toValue: 1,
            duration: 1400,
            useNativeDriver: false,
          }),
          Animated.timing(pulseAnim, {
            toValue: 0,
            duration: 1400,
            useNativeDriver: false,
          }),
        ]),
      ).start();
    } else {
      pulseAnim.stopAnimation();
      pulseAnim.setValue(0);
    }
  }, [listening]);

  const borderColor = pulseAnim.interpolate({
    inputRange: [0, 1],
    outputRange: ["rgba(0, 255, 85, 0.35)", "rgba(0, 255, 85, 1)"],
  });

  return (
    <View className="flex-1 items-center justify-start pt-28 bg-background">
      <View
        className={`w-75 h-75 rounded-full border-[0.5px] items-center justify-center ${
          listening ? "animate-ripple-3" : "border-[#0a150a]"
        }`}
      >
        <View
          className={`w-65.5 h-65.5 rounded-full border-[0.5px] items-center justify-center ${
            listening ? "animate-ripple-2" : "border-[#0f200f]"
          }`}
        >
          <View
            className={`w-56.5 h-56.5 rounded-full border items-center justify-center ${
              listening ? "animate-ripple-1" : "border-main/10"
            }`}
          >
            <AnimatedPressable
              onPress={() => setListening(!listening)}
              className="w-38 h-38 rounded-full border-[1.5px] bg-background items-center justify-center"
              style={{
                borderColor: listening ? borderColor : "rgba(0, 255, 85, 0.4)",
              }}
            >
              <Mic color="#00ff55" size={50} strokeWidth={1.5} />
            </AnimatedPressable>
          </View>
        </View>
      </View>

      <Text
        className={`mt-8 text-[11px] tracking-[3px] uppercase font-mono ${
          listening ? "text-main" : "text-muted"
        }`}
      >
        {listening ? "listening..." : "tap to speak"}
      </Text>
    </View>
  );
};

export default VoiceNode;