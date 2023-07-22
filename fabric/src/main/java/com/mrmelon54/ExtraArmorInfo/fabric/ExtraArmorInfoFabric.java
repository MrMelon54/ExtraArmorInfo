package com.mrmelon54.ExtraArmorInfo.fabric;

import com.mrmelon54.ExtraArmorInfo.fabriclike.ExtraArmorInfoFabricLike;
import net.fabricmc.api.ModInitializer;

public class ExtraArmorInfoFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        ExtraArmorInfoFabricLike.init();
    }
}
