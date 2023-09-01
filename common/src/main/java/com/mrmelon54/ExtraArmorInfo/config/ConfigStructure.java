package com.mrmelon54.ExtraArmorInfo.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "extra_armor_info")
@Config.Gui.Background("minecraft:textures/block/netherite_block.png")
public class ConfigStructure implements ConfigData {
    @ConfigEntry.Gui.Tooltip
    public boolean mergeKnockbackAndToughness = true;

    @ConfigEntry.Category("knockback")
    @ConfigEntry.Gui.TransitiveObject
    public SubConfig knockback = new SubConfig();

    @ConfigEntry.Category("toughness")
    @ConfigEntry.Gui.TransitiveObject
    public SubConfig toughness = new SubConfig();

    @ConfigEntry.Category("hot-icons")
    @ConfigEntry.Gui.TransitiveObject
    public SubConfig hotIcons = new SubConfig();
}
