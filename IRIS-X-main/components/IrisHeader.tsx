import { LucideBellDot, SignalHighIcon } from "lucide-react-native";
import React from "react";
import { Text, TouchableOpacity, View } from "react-native";

const IrisHeader = () => {
  return (
    <>
      <View className="border-b border-primary w-full justify-between px-5 pb-5 items-center flex-row">
        <SignalHighIcon color="#02c944" size={32} />
        <View>
          <Text className="text-white text-3xl text-center font-antonio-bold tracking-wider">
            IRIS
            <Text className="text-main">-X</Text>
          </Text>
        </View>
        <View>
          <TouchableOpacity>
            <LucideBellDot color="#02c944" size={28} />
          </TouchableOpacity>
        </View>
      </View>
    </>
  );
};

export default IrisHeader;
