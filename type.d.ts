import { ImageSourcePropType } from "react-native";

declare global {
  interface AppTab {
    name: string;
    title: string;
    icon: ImageSourcePropType;
  }
  interface TabIconProps {
    focused: boolean;
    icon: ImageSourcePropType;
  }
}

export {};