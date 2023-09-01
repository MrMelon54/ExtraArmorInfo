package com.mrmelon54.ExtraArmorInfo.forge;

import com.mrmelon54.ExtraArmorInfo.ExtraArmorInfo;
import com.mrmelon54.ExtraArmorInfo.ExtraArmorInfoHudRenderer;
import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.client.ConfigScreenHandler.ConfigScreenFactory;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(ExtraArmorInfo.MOD_ID)
public class ExtraArmorInfoForge {
    public ExtraArmorInfoForge() {
        // Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(ExtraArmorInfo.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        ModLoadingContext.get().registerExtensionPoint(ConfigScreenFactory.class, () -> new ConfigScreenFactory((mc, screen) -> ExtraArmorInfo.createConfigScreen(screen).get()));

        ExtraArmorInfo.init();
    }
}
