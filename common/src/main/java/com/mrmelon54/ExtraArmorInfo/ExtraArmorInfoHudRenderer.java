package com.mrmelon54.ExtraArmorInfo;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mrmelon54.ExtraArmorInfo.config.ConfigStructure;
import com.mrmelon54.ExtraArmorInfo.config.SubConfig;
import com.mrmelon54.ExtraArmorInfo.enums.PositionDisplayOption;
import com.mrmelon54.ExtraArmorInfo.enums.SideDisplayOption;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;

import java.util.Iterator;

public class ExtraArmorInfoHudRenderer {
    public static final ResourceLocation EXTRA_ARMOUR_INFO_ICONS_TEXTURE = new ResourceLocation("extra_armor_info:textures/gui/extra_armor_info.png");

    public static void renderHud(GuiGraphics graphics, float tickDelta) {
        ConfigStructure config = ExtraArmorInfo.getConfig();
        Minecraft client = Minecraft.getInstance();
        Entity cameraEntity = client.getCameraEntity();
        if (client.player == null || cameraEntity == null) return;
        Iterable<ItemStack> armorSlots = cameraEntity.getArmorSlots();

        float knockbackValue = 0;
        int toughnessValue = 0;
        for (ItemStack armorSlot : armorSlots) {
            if (armorSlot.getItem() instanceof ArmorItem armorItem) {
                knockbackValue += armorItem.getMaterial().getKnockbackResistance();
                toughnessValue += (int) armorItem.getToughness();
            }
        }

        // knockback value is 0.1 per netherite armor
        // multiply by 10 to make it 1 per netherite armor
        // multiply by 2 to make it take up a full icon above the hotbar
        final int kValue = config.knockback.enabled ? Mth.floor(knockbackValue * 20) : 0;
        final int tValue = config.toughness.enabled ? Mth.floor(toughnessValue) : 0;

        renderHudIcons(graphics, tickDelta, client.getProfiler(), client.player, config, kValue, tValue);
        if (config.hotIcons.enabled)
            renderArmorIcons(graphics, tickDelta, armorSlots.iterator(), client.player, config.hotIcons);
    }

    private static void renderHudIcons(GuiGraphics graphics, float tickDelta, ProfilerFiller profiler, LocalPlayer player, ConfigStructure config, int kValue, int tValue) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        int w = graphics.guiWidth();
        int h = graphics.guiHeight();

        // just a copy of some math in the vanilla renderStatusBars method
        int w2 = w / 2;
        int adjustHeight = h - 39;
        float maxHealth = (float) player.getAttributeValue(Attributes.MAX_HEALTH);
        int absorption = Mth.ceil(player.getAbsorptionAmount());

        // calculate space needed for hearts
        int q = Mth.ceil((maxHealth + (float) absorption) / 2.0F / 10.0F);
        int r = Math.max(10 - (q - 2), 3);
        int s = adjustHeight - (q - 1) * r - 10;

        int leftX = w2 - 91; // 91 is the icon furthest to the left
        int rightX = w2 + 91 - 9 * 9; // 91 is the icon furthest to the right
        int leftY = s - 10; // Leave 10 pixels for health bar
        int rightY = adjustHeight - 10; // Leave 10 pixels for hunger bar

        int farLeftX = 1;
        int farMiddleRightX = w - 10;
        int farRightX = w - 1 - 81; // 81 is the width of the full status bar
        int farMiddleY = (h - 81) / 2 + 1; // use half of 81 here
        int farBottomY = h - 10;

        int ah = player.getMaxAirSupply();
        int ai = Math.min(player.getAirSupply(), ah);

        if (player.isEyeInFluid(FluidTags.WATER) || ai < ah) rightY -= 10; // Make room for bubbles when in water

        int toughnessPosX = getPosFromPositionAndSide(config.toughness, leftX, rightX, farLeftX, farRightX, farLeftX, farMiddleRightX, farLeftX, farRightX);
        int toughnessPosY = getPosFromPositionAndSide(config.toughness, leftY, rightY, 1, 1, farMiddleY, farMiddleY, farBottomY, farBottomY);
        int knockbackPosX = getPosFromPositionAndSide(config.knockback, leftX, rightX, farLeftX, farRightX, farLeftX, farMiddleRightX, farLeftX, farRightX);
        int knockbackPosY = getPosFromPositionAndSide(config.knockback, leftY, rightY, 1, 1, farMiddleY, farMiddleY, farBottomY, farBottomY);

        if (config.hotIcons.overlaps(config.toughness)) {
            toughnessPosY += getYFromPosition(config.hotIcons, 0, 16, 0, -16);
            if (config.hotIcons.position == PositionDisplayOption.MIDDLE)
                toughnessPosX += config.hotIcons.side == SideDisplayOption.LEFT ? 16 : -16;
        }
        if (config.hotIcons.overlaps(config.knockback)) {
            knockbackPosY += getYFromPosition(config.hotIcons, 0, 16, 0, -16);
            if (config.hotIcons.position == PositionDisplayOption.MIDDLE)
                knockbackPosX += config.hotIcons.side == SideDisplayOption.LEFT ? 16 : -16;
        }

        // Move knockback resistance bar to leave space for toughness bar if they are both displayed in the same part of the screen
        boolean iconOverlap = config.toughness.overlaps(config.knockback);

        profiler.push("extra_armor_info");

        int x = toughnessPosX + config.toughness.tweakX;
        int y = toughnessPosY + config.toughness.tweakY;

        boolean isTMiddle = config.toughness.position == PositionDisplayOption.MIDDLE;
        boolean isKMiddle = config.knockback.position == PositionDisplayOption.MIDDLE;

        // display knockback and toughness from opposite sides of the same row if they can fit on a single line without overlapping
        if (config.mergeKnockbackAndToughness && iconOverlap && tValue + kValue <= 20) {
            boolean invert = !isTMiddle && config.toughness.side == SideDisplayOption.RIGHT;
            drawSingleBar(graphics, x, y, 0, tValue, Mth.ceil(tValue / 2f), isTMiddle, invert);
            drawSingleBar(graphics, x, y, 9, kValue, Mth.ceil(kValue / 2f), isTMiddle, !invert);
        } else {
            for (int i = 0; i < tValue; i += 20) {
                drawSingleBar(graphics, x, y, 0, tValue - i, 10, isTMiddle, !isTMiddle && config.toughness.side == SideDisplayOption.RIGHT);
                y += armorIconRowOffset(config.toughness.position);
                if (isTMiddle)
                    x += config.toughness.side == SideDisplayOption.LEFT ? 9 : -9;
            }
            if (!iconOverlap) {
                x = knockbackPosX + config.knockback.tweakX;
                y = knockbackPosY + config.knockback.tweakY;
            }
            for (int i = 0; i < kValue; i += 20) {
                drawSingleBar(graphics, x, y, 9, kValue - i, 10, isKMiddle, !isKMiddle && config.knockback.side == SideDisplayOption.RIGHT);
                y += armorIconRowOffset(config.knockback.position);
                if (isKMiddle)
                    x += config.knockback.side == SideDisplayOption.LEFT ? 9 : -9;
            }
        }

        profiler.pop();

        RenderSystem.disableBlend();
    }

    private static void drawSingleBar(GuiGraphics graphics, int x, int y, int tex, int value, int total, boolean vertical, boolean inverted) {
        // don't render a 0 value
        if (value <= 0) return;
        for (int i = 0; i < total; ++i) {
            int iconPos = i * 8;
            int offset = inverted ? 9 * 8 - iconPos : iconPos;
            int screenX = vertical ? x : x + offset;
            int screenY = vertical ? y + offset : y;
            if (i * 2 + 1 < value)
                // Draw the fully filled icon
                graphics.blit(EXTRA_ARMOUR_INFO_ICONS_TEXTURE, screenX, screenY, 18, tex, 9, 9, 32, 32);
            else if (i * 2 + 1 == value) {
                // Draw half the empty icon and the other half of the split icon
                int tweakSide = (inverted ? 0 : 1) * 4;
                graphics.blit(EXTRA_ARMOUR_INFO_ICONS_TEXTURE, screenX + tweakSide, screenY, tweakSide, tex, 5, 9, 32, 32);
                graphics.blit(EXTRA_ARMOUR_INFO_ICONS_TEXTURE, screenX + 4 - tweakSide, screenY, 9 + 4 - tweakSide, tex, 5, 9, 32, 32);
            } else if (i * 2 + 1 > value)
                // Draw the empty icon
                graphics.blit(EXTRA_ARMOUR_INFO_ICONS_TEXTURE, screenX, screenY, 0, tex, 9, 9, 32, 32);
        }
    }

    private static void renderArmorIcons(GuiGraphics graphics, float tickDelta, Iterator<ItemStack> armorSlots, LocalPlayer player, SubConfig subConfig) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        PoseStack pose = graphics.pose();
        pose.pushPose();
        pose.translate(0, 0, -200);

        boolean vertical = subConfig.position == PositionDisplayOption.HUD || subConfig.position == PositionDisplayOption.MIDDLE;
        int w = graphics.guiWidth();
        int w2 = w / 2;
        int h = graphics.guiHeight();
        int h2 = h / 2;
        int x = getPosFromPositionAndSide(subConfig, w2 - getHotBarOffset(player, HumanoidArm.LEFT) - 16, w2 + getHotBarOffset(player, HumanoidArm.RIGHT), 0, w - 4 * 16, 0, w - 16, 0, w - 4 * 16);
        int y = getYFromPosition(subConfig, h - 4 * 16, 0, h2 - 32, h - 16);
        x += subConfig.tweakX;
        y += subConfig.tweakY;
        if (vertical) y += 3 * 16;
        else x += 3 * 16;
        for (int i = 0; i < 4; i++)
            if (armorSlots.hasNext()) {
                graphics.renderItem(armorSlots.next(), x, y, 0);
                if (vertical) y -= 16;
                else x -= 16;
            }

        pose.popPose();
        RenderSystem.disableBlend();
    }

    private static int getHotBarOffset(LocalPlayer player, HumanoidArm humanoidArm) {
        return 91 + 8 + (!player.getOffhandItem().isEmpty() && player.getMainArm().getOpposite() == humanoidArm ? 29 : 0);
    }

    private static int armorIconRowOffset(PositionDisplayOption position) {
        return switch (position) {
            case HUD, BOTTOM -> -10;
            case TOP -> +10;
            case MIDDLE -> 0; // renders horizontally
        };
    }

    private static int getYFromPosition(SubConfig config, int hud, int top, int middle, int bottom) {
        return switch (config.position) {
            case HUD -> hud;
            case TOP -> top;
            case MIDDLE -> middle;
            case BOTTOM -> bottom;
        };
    }

    private static int getPosFromPositionAndSide(SubConfig config, int hudLeft, int hudRight, int topLeftY, int topRightY, int middleLeftY, int middleRightY, int bottomLeftY, int bottomRightY) {
        boolean isLeft = config.side == SideDisplayOption.LEFT;
        return switch (config.position) {
            case HUD -> isLeft ? hudLeft : hudRight;
            case TOP -> isLeft ? topLeftY : topRightY;
            case MIDDLE -> isLeft ? middleLeftY : middleRightY;
            case BOTTOM -> isLeft ? bottomLeftY : bottomRightY;
        };
    }
}
