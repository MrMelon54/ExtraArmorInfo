package com.mrmelon54.ExtraArmorInfo.forge;

import dev.architectury.platform.forge.EventBuses;
import com.mrmelon54.ExtraArmorInfo.ExtraArmorInfo;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(ExtraArmorInfo.MOD_ID)
public class ExtraArmorInfoForge {
    public ExtraArmorInfoForge() {
        // Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(ExtraArmorInfo.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        ExtraArmorInfo.init();
    }
}
