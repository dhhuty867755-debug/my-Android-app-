import IrisBiometricScanner from "@/components/IrisBiometricScanner";
import IrisHeader from "@/components/IrisHeader";
import IrisHolographicOrb from "@/components/IrisHolographicOrb";
import IrisNativeVisualizer from "@/components/IrisNativeVisualizer";
import IrisQuantumHUD from "@/components/IrisQuantumHUD";
import VoiceNode from "@/components/VoiceNode";
import { styled } from "nativewind";
import { ScrollView } from "react-native";
import { SafeAreaView as RNSafeAreaView } from "react-native-safe-area-context";

const SafeAreaView = styled(RNSafeAreaView);

export default function Index() {
  return (
    <SafeAreaView className="flex-1 bg-black p-5">
      <ScrollView showsVerticalScrollIndicator={false} contentContainerStyle={{ paddingBottom: 32 }}>
        <IrisHeader />
        <IrisHolographicOrb />
        <VoiceNode />
        <IrisBiometricScanner />
        <IrisNativeVisualizer />
        <IrisQuantumHUD />
      </ScrollView>
    </SafeAreaView>
  );
}
