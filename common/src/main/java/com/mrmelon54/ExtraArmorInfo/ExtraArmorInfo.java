package com.mrmelon54.ExtraArmorInfo;

public class ExtraArmorInfo {
    public static final String MOD_ID = "extra_armor_info";

    public static void init() {
        System.out.println(ExampleExpectPlatform.getConfigDirectory().toAbsolutePath().normalize().toString());
    }
}
