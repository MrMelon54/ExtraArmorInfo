package com.mrmelon54.ExtraArmorInfo;

import com.mrmelon54.ExtraArmorInfo.config.ConfigStructure;
import dev.architectury.event.events.client.ClientGuiEvent;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;

import java.util.function.Supplier;

public class ExtraArmorInfo {
    public static final String MOD_ID = "extra_armor_info";
    private static ConfigStructure config;

    public static ConfigStructure getConfig() {
        return config;
    }

    public static void init() {
        AutoConfig.register(ConfigStructure.class, JanksonConfigSerializer::new);
        config = AutoConfig.getConfigHolder(ConfigStructure.class).getConfig();
    }

    public static Supplier<Screen> createConfigScreen(Screen screen) {
        return AutoConfig.getConfigScreen(ConfigStructure.class, screen);
    }
}
