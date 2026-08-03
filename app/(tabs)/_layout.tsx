import { tabs } from "@/constants/data";
import { components } from "@/constants/theme";
import { Image } from "expo-image";
import { Tabs } from "expo-router";
import { View } from "react-native";
import { useSafeAreaInsets } from "react-native-safe-area-context";

const tabBar = components.tabBar;

const TabIcon = ({ focused, icon }: TabIconProps) => {
  return (
    <>
      <View
        className={`items-center justify-center w-14 h-14 rounded-full ${
          focused ? "bg-primary" : "bg-transparent"
        }`}
        style={
          focused
            ? {
                shadowColor: "#1B3B24",
                shadowOffset: { width: 0, height: 0 },
                shadowOpacity: 0.8,
                shadowRadius: 14,
                elevation: 4,
              }
            : {}
        }
      >
        <Image
          source={icon}
          contentFit="contain"
          tintColor={focused ? "#000" : "#fff"}
          style={{ width: 24, height: 24 }}
        />
      </View>
    </>
  );
};

const TabLayout = () => {
  const insets = useSafeAreaInsets();

  return (
    <Tabs
      screenOptions={{
        headerShown: false,
        tabBarShowLabel: false,
        tabBarStyle: {
          position: "absolute",
          bottom: Math.max(insets.bottom, tabBar.horizontalInset),
          height: tabBar.height,
          marginHorizontal: tabBar.horizontalInset,
          borderRadius: tabBar.radius,
          backgroundColor: "#000",
          borderTopWidth: 0,
          elevation: 0,
        },
        tabBarItemStyle: {
          paddingVertical: tabBar.height / 2 - tabBar.iconFrame / 1.6,
        },
        tabBarIconStyle: {
          width: tabBar.iconFrame,
          height: tabBar.iconFrame,
          alignItems: "center",
        },
      }}
    >
      {tabs.map((tab) => (
        <Tabs.Screen
          key={tab.name}
          name={tab.name}
          options={{
            title: tab.title,
            tabBarIcon: ({ focused }) => (
              <TabIcon focused={focused} icon={tab.icon} />
            ),
          }}
        />
      ))}
    </Tabs>
  );
};

export default TabLayout;
