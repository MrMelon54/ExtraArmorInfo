package com.mrmelon54.ExtraArmorInfo.mixin;

import com.mrmelon54.ExtraArmorInfo.ExtraArmorInfoHudRenderer;
import dev.architectury.platform.Platform;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public abstract class MixinGui {
    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;renderPlayerHealth(Lnet/minecraft/client/gui/GuiGraphics;)V"))
    private void renderExtraArmorInfo(GuiGraphics guiGraphics, float f, CallbackInfo ci) {
        // This will never get called on forge but just to be safe we ignore that platform
        if (Platform.isForge()) return;
        ExtraArmorInfoHudRenderer.renderHud(guiGraphics, f);
    }
}
