package com.mrmelon54.ExtraArmorInfo.quilt;

import com.mrmelon54.ExtraArmorInfo.fabriclike.ExtraArmorInfoFabricLike;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;

public class ExtraArmorInfoQuilt implements ModInitializer {
    @Override
    public void onInitialize(ModContainer mod) {
        ExtraArmorInfoFabricLike.init();
    }
}
