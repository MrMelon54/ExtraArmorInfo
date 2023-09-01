package com.mrmelon54.ExtraArmorInfo.forge;

import com.mrmelon54.ExtraArmorInfo.ExtraArmorInfoHudRenderer;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class EventHandler {
    @SubscribeEvent
    public static void guiEventHandler(RenderGuiEvent event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.options.hideGui || mc.gameMode == null || !mc.gameMode.canHurtPlayer()) return;
        ExtraArmorInfoHudRenderer.renderHud(event.getGuiGraphics(), event.getPartialTick());
    }
}
