package com.mrmelon54.ExtraArmorInfo.config;

import com.mrmelon54.ExtraArmorInfo.enums.PositionDisplayOption;
import com.mrmelon54.ExtraArmorInfo.enums.SideDisplayOption;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

public class SubConfig implements ConfigData {
    public boolean enabled = true;

    @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
    public PositionDisplayOption position = PositionDisplayOption.HUD;

    @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
    public SideDisplayOption side = SideDisplayOption.LEFT;

    @ConfigEntry.Gui.Tooltip()
    public int tweakX = 0;

    @ConfigEntry.Gui.Tooltip()
    public int tweakY = 0;

    public boolean overlaps(SubConfig subConfig) {
        return enabled && subConfig.enabled && position == subConfig.position && side == subConfig.side;
    }
}
